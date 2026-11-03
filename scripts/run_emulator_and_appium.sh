#!/bin/zsh

set -euo pipefail

AVD_NAME="${AVD_NAME:-Pixel_5}"
EMULATOR_PORT="${EMULATOR_PORT:-5554}"
APPIUM_PORT="${APPIUM_PORT:-4723}"
APPIUM_HOST="${APPIUM_HOST:-127.0.0.1}"
SERIAL="emulator-${EMULATOR_PORT}"
STATUS_URL="http://${APPIUM_HOST}:${APPIUM_PORT}/status"

wait_for_boot() {
  local serial="$1"

  adb -s "$serial" wait-for-device

  while [[ "$(adb -s "$serial" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" != "1" ]]; do
    sleep 1
  done
}

wake_and_unlock() {
  local serial="$1"

  adb -s "$serial" shell input keyevent KEYCODE_WAKEUP || true
  sleep 1
  adb -s "$serial" shell input keyevent 82 || true
}

is_appium_running() {
  curl -sf --max-time 2 "$STATUS_URL" >/dev/null 2>&1
}

if ! command -v emulator >/dev/null 2>&1; then
  echo "emulator not found in PATH" >&2
  exit 1
fi

if ! command -v adb >/dev/null 2>&1; then
  echo "adb not found in PATH" >&2
  exit 1
fi

if ! command -v appium >/dev/null 2>&1; then
  echo "appium not found in PATH" >&2
  exit 1
fi

if ! command -v curl >/dev/null 2>&1; then
  echo "curl not found in PATH" >&2
  exit 1
fi

echo "Starting AVD=${AVD_NAME} on port ${EMULATOR_PORT}"
emulator -avd "$AVD_NAME" -port "$EMULATOR_PORT" -no-snapshot-save &

echo "Waiting for device ${SERIAL}"
wait_for_boot "$SERIAL"
wake_and_unlock "$SERIAL"

if is_appium_running; then
  echo "Appium already running on ${APPIUM_HOST}:${APPIUM_PORT}, skip start"
  exit 0
fi

echo "Starting Appium on port ${APPIUM_PORT} with inspector plugin"
exec appium -p "$APPIUM_PORT" \
  --use-plugins=inspector \
  --allow-insecure='*:adb_shell,*:session_discovery'
