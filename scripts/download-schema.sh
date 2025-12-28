#!/bin/bash

# GraphQLスキーマをダウンロードするスクリプト

ENDPOINT="https://graphql.pokeapi.co/v1beta2"
SCHEMA_PATH="shared/src/commonMain/graphql/schema.graphqls"

echo "Downloading GraphQL schema from $ENDPOINT..."

./gradlew :shared:downloadApolloSchema \
  --endpoint="$ENDPOINT" \
  --schema="$SCHEMA_PATH"

if [ $? -eq 0 ]; then
  echo "Schema downloaded successfully to $SCHEMA_PATH"
else
  echo "Failed to download schema"
  exit 1
fi
