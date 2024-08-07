syndicate generate meta s3_bucket \
    --resource_name api-ui-hoster \
    --location eu-central-1 \
    --acl private \
    --block_public_acls false \
    --ignore_public_acls false \
    --block_public_policy false \
    --restrict_public_buckets false \
	  --static_website_hosting true

syndicate generate swagger_ui \
	--name swagger_ui \
	--path_to_spec export/5dg8cekdee_oas_v3.json \
	--target_bucket api-ui-hoster