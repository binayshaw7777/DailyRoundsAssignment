#!/bin/bash
set -e

PACKAGE="com.binayshaw7777.dailyroundsassignment.debug"
DEVICE_PATH="/data/local/tmp/ComposePreviews"
LOCAL_PATH="./previews"

echo "Running preview export tests..."
./gradlew :app:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.binayshaw7777.dailyroundsassignment.PreviewExportTest

echo "Pulling screenshots from device..."
mkdir -p "$LOCAL_PATH"
adb pull "$DEVICE_PATH/." "$LOCAL_PATH/"

echo "Done. Previews saved to $LOCAL_PATH/"
ls "$LOCAL_PATH/"
