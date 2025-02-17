#!/usr/bin/env sh

set -e

if [ "$1" = 'java' ]; then
  shift

  DEFAULT_JVM_HEAP_SIZE_PERCENT=75

  if [ -z "$JVM_HEAP_SIZE_PERCENT" ]; then
    JVM_HEAP_SIZE_PERCENT=$DEFAULT_JVM_HEAP_SIZE_PERCENT
  fi

  if [ -n "$REELED_TOTAL_CONTAINER_MEMORY" ]; then
    JVM_HEAP_SIZE_MB=$(((REELED_TOTAL_CONTAINER_MEMORY - 256 * 1024 * 1024) * JVM_HEAP_SIZE_PERCENT / (100 * 1024 * 1024)))
    JVM_HEAP_SIZE_ARGS="-Xms${JVM_HEAP_SIZE_MB}M -Xmx${JVM_HEAP_SIZE_MB}M"
  else
    JVM_HEAP_SIZE_ARGS="-Xms512M -Xmx512M"
  fi

  JVM_OTHER_OPTIONS="-XX:+AlwaysPreTouch"

  # shellcheck disable=SC2086
  echo "JVM Options:" $JVM_HEAP_SIZE_ARGS $JVM_OTHER_OPTIONS

  # shellcheck disable=SC2086
  java $JVM_HEAP_SIZE_ARGS $JVM_OTHER_OPTIONS "$@"
else
  exec "$@"
fi
