#!/bin/bash

# Script to pull Compose preview screenshots from device to project folder
# Usage: ./export-previews.sh

PREVIEW_DIR="previews"
DEVICE_DIR="/sdcard/Pictures/ComposePreviews"

echo "Creating local preview directory..."
mkdir -p "$PREVIEW_DIR"

echo "Pulling screenshots from device..."
adb pull "$DEVICE_DIR/." "$PREVIEW_DIR/"

echo "Screenshots exported to $PREVIEW_DIR/"
ls -la "$PREVIEW_DIR/"

echo "Done!"
