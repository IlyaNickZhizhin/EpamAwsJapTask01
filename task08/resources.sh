#!/bin/bash

syndicate generate lambda --name "weather_client" --runtime "java"

syndicate generate lambda_layer --name "weather_api" --runtime "java" --link_with_lambda "weather_client" --project_path "./lib/"