#!/usr/bin/env bash
# Automates the manual verification steps from the README: build the container,
# wait for it to report healthy, then check the documented /api/ping shape.
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")/.."

cleanup() {
    docker compose down -v
}
trap cleanup EXIT

docker compose up --build -d

echo "Waiting for /actuator/health to report UP..."
attempts=30
until curl -sf localhost:8080/actuator/health | grep -q '"status":"UP"'; do
    attempts=$((attempts - 1))
    if [ "$attempts" -le 0 ]; then
        echo "FAIL: service did not become healthy in time" >&2
        exit 1
    fi
    sleep 1
done
echo "OK: health check UP"

echo "Checking /api/ping..."
if ! curl -sf localhost:8080/api/ping | grep -q '"shipmentCount":1'; then
    echo "FAIL: /api/ping did not report shipmentCount 1" >&2
    exit 1
fi
echo "OK: /api/ping reports shipmentCount 1"

echo "Smoke test passed."
