#!/bin/bash

#syndicate generate lambda --name api_handler --runtime java

syndicate generate meta dynamodb --resource_name Tables --hash_key_name "key" --hash_key_type S --read_capacity 1 --write_capacity 1

syndicate generate meta dynamodb --resource_name Reservations --hash_key_name id --hash_key_type S --read_capacity 1 --write_capacity 1

syndicate generate meta api_gateway --resource_name task10_api \
  --deploy_stage api \
  --minimum_compression_size 0

#verify custom_attributes in deployment_resources.json
# {
#          "name": "first_name",
#          "type": "String"
#        },
#        {
#          "name": "last_name",
#          "type": "String"
#        }
#add "client": {
#      "client_name": "client-app",
#      "generate_secret": false,
#      "explicit_auth_flows": [
#        "ALLOW_ADMIN_USER_PASSWORD_AUTH",
#        "ALLOW_CUSTOM_AUTH",
#        "ALLOW_USER_SRP_AUTH",
#        "ALLOW_REFRESH_TOKEN_AUTH"
#      ]
#    }
syndicate generate meta cognito_user_pool --resource_name simple-booking-userpool \
  --custom_attributes first_name String \
  --custom_attributes last_name String

syndicate generate meta api_gateway_authorizer \
  --api_name task10_api \
  --name authorizer \
  --type COGNITO_USER_POOLS \
  --provider_name simple-booking-userpool

#################################### add to jsons -> "integration_passthrough_behavior": "WHEN_NO_TEMPLATES"
syndicate generate meta api_gateway_resource --api_name task10_api --path signup --enable_cors false

#add "enable_proxy": true in deployment_resources.json
syndicate generate meta api_gateway_resource_method --api_name task10_api --path signup \
  --method POST --integration_type lambda --lambda_name "api_handler"

#################################### add to jsons -> "integration_passthrough_behavior": "WHEN_NO_TEMPLATES"
syndicate generate meta api_gateway_resource --api_name task10_api --path signin --enable_cors true

#add "enable_proxy": true in deployment_resources.json
syndicate generate meta api_gateway_resource_method --api_name task10_api --path signin \
  --method POST --integration_type lambda --lambda_name "api_handler"

#################################### add to jsons -> "integration_passthrough_behavior": "WHEN_NO_TEMPLATES"
syndicate generate meta api_gateway_resource --api_name task10_api --path tables --enable_cors false
syndicate generate meta api_gateway_resource --api_name task10_api --path tables/{tableId} --enable_cors false

#replace NONE on authorizer in deployment_resources.json
#add "enable_proxy": true in deployment_resources.json
syndicate generate meta api_gateway_resource_method --api_name task10_api --path tables \
  --method POST --integration_type lambda --lambda_name "api_handler" --authorization_type NONE

#replace NONE on authorizer in deployment_resources.json
#add "enable_proxy": true in deployment_resources.json
syndicate generate meta api_gateway_resource_method --api_name task10_api --path tables \
  --method GET --integration_type lambda --lambda_name "api_handler" --authorization_type NONE

#replace NONE on authorizer in deployment_resources.json
#add "enable_proxy": true in deployment_resources.json
syndicate generate meta api_gateway_resource_method --api_name task10_api --path tables\{tableId} \
  --method GET --integration_type lambda --lambda_name "api_handler" --authorization_type NONE

#################################### add to jsons -> "integration_passthrough_behavior": "WHEN_NO_TEMPLATES"
syndicate generate meta api_gateway_resource --api_name task10_api --path reservations --enable_cors false

#replace NONE on authorizer in deployment_resources.json
#add "enable_proxy": true in deployment_resources.json
syndicate generate meta api_gateway_resource_method --api_name task10_api --path reservations \
  --method POST --integration_type lambda --lambda_name "api_handler" --authorization_type NONE

#replace NONE on authorizer in deployment_resources.json
#add "enable_proxy": true in deployment_resources.json
syndicate generate meta api_gateway_resource_method --api_name task10_api --path reservations \
  --method GET --integration_type lambda --lambda_name "api_handler" --authorization_type NONE
