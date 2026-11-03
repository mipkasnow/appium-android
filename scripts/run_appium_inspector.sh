#!/bin/zsh

set -euo pipefail

APPIUM_PORT="${APPIUM_PORT:-4723}"
APPIUM_HOST="${APPIUM_HOST:-127.0.0.1}"
OPEN_BROWSER="${OPEN_BROWSER:-1}"
FORCE_RESTART="${FORCE_RESTART:-0}"
INSPECTOR_URL="http://${APPIUM_HOST}:${APPIUM_PORT}/inspector"
STATUS_URL="http://${APPIUM_HOST}:${APPIUM_PORT}/status"
APPIUM_STARTED_BY_SCRIPT=0
APPIUM_PID=""

is_appium_running() {
  curl -sf --max-time 2 "$STATUS_URL" >/dev/null 2>&1
}

is_inspector_available() {
  curl -sf --max-time 2 -o /dev/null "$INSPECTOR_URL"
}

wait_for_appium() {
  local attempts=60
  local i

  for i in $(seq 1 "$attempts"); do
    if is_appium_running; then
      return 0
    fi
    sleep 0.5
  done

  echo "Appium did not become ready at ${STATUS_URL}" >&2
  return 1
}

wait_for_inspector() {
  local attempts=60
  local i

  for i in $(seq 1 "$attempts"); do
    if is_inspector_available; then
      return 0
    fi
    sleep 0.5
  done

  echo "Inspector did not become ready at ${INSPECTOR_URL}" >&2
  return 1
}

wait_for_port_free() {
  local attempts=40
  local i

  for i in $(seq 1 "$attempts"); do
    if ! lsof -nP -iTCP:"$APPIUM_PORT" -sTCP:LISTEN >/dev/null 2>&1; then
      return 0
    fi
    sleep 0.25
  done

  echo "Port ${APPIUM_PORT} is still in use" >&2
  return 1
}

stop_appium_on_port() {
  local pids
  pids="$(lsof -nP -iTCP:"$APPIUM_PORT" -sTCP:LISTEN -t 2>/dev/null || true)"

  if [[ -z "$pids" ]]; then
    return 0
  fi

  echo "Stopping process(es) on port ${APPIUM_PORT}: ${pids}"
  echo "$pids" | xargs kill 2>/dev/null || true

  local pid
  for pid in ${(f)pids}; do
    wait "$pid" 2>/dev/null || true
  done

  if lsof -nP -iTCP:"$APPIUM_PORT" -sTCP:LISTEN >/dev/null 2>&1; then
    echo "$pids" | xargs kill -9 2>/dev/null || true
  fi

  wait_for_port_free
}

start_appium_with_inspector() {
  echo "Starting Appium on port ${APPIUM_PORT} with inspector plugin"
  appium -p "$APPIUM_PORT" \
    --use-plugins=inspector \
    --allow-insecure='*:adb_shell,*:session_discovery' &
  APPIUM_PID=$!
  APPIUM_STARTED_BY_SCRIPT=1

  wait_for_appium
  wait_for_inspector
}

open_inspector() {
  if [[ "$OPEN_BROWSER" != "1" ]]; then
    echo "Inspector URL: ${INSPECTOR_URL}"
    return 0
  fi

  echo "Opening ${INSPECTOR_URL}"
  open "$INSPECTOR_URL"
}

cleanup() {
  if [[ "$APPIUM_STARTED_BY_SCRIPT" -eq 1 && -n "$APPIUM_PID" ]] && kill -0 "$APPIUM_PID" 2>/dev/null; then
    kill "$APPIUM_PID" 2>/dev/null || true
    wait "$APPIUM_PID" 2>/dev/null || true
  fi
}
trap cleanup EXIT INT TERM

if ! command -v appium >/dev/null 2>&1; then
  echo "appium not found in PATH" >&2
  exit 1
fi

if ! command -v curl >/dev/null 2>&1; then
  echo "curl not found in PATH" >&2
  exit 1
fi

if ! command -v lsof >/dev/null 2>&1; then
  echo "lsof not found in PATH" >&2
  exit 1
fi

if [[ "$FORCE_RESTART" == "1" ]]; then
  echo "FORCE_RESTART=1, restarting Appium with inspector"
  stop_appium_on_port
  start_appium_with_inspector
elif is_appium_running; then
  if is_inspector_available; then
    echo "Appium with inspector already running on ${APPIUM_HOST}:${APPIUM_PORT}, skip start"
    open_inspector
    trap - EXIT INT TERM
    exit 0
  fi

  echo "Appium is running without inspector, restarting with --use-plugins=inspector"
  stop_appium_on_port
  start_appium_with_inspector
else
  start_appium_with_inspector
fi

open_inspector

echo "Appium Inspector is running (pid=${APPIUM_PID}). Press Ctrl+C to stop."
wait "$APPIUM_PID"
