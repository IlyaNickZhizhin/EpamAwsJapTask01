package com.task07;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syndicate.deployment.annotations.events.RuleEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.syndicate.deployment.model.RetentionSetting;
import com.task07.dto.Ids;
import com.task07.imports.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static com.task07.imports.Import.S3_NAME;

@LambdaHandler(lambdaName = "uuid_generator",
	roleName = "uuid_generator-role",
	isPublishVersion = false,
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@RuleEventSource(targetRule = "uuid_trigger")
@DependsOn(name = "uuid_trigger", resourceType = ResourceType.CLOUDWATCH_RULE)
public class UuidGenerator implements RequestHandler<Object, Map<String, Object>> {

	private final Gson gson= new GsonBuilder().setPrettyPrinting().create();
	private final String S3_NAME = Import.S3_FULL_NAME;

	public Map<String, Object> handleRequest(Object request, Context context) {
		Ids ids = new Ids();
		List<String> uuids =
				Stream.generate(UUID::randomUUID).limit(10)
						.map(UUID::toString)
						.collect(Collectors.toList());
		ids.setIds(uuids);
		AmazonS3 amazonS3 = AmazonS3ClientBuilder
				.standard()
				.withRegion("eu-central-1")
				.build();
		String fileName = LocalDateTime.now().toString();
		amazonS3.putObject(S3_NAME, fileName, gson.toJson(ids));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("statusCode", 200);
		resultMap.put("body", "uuids added");
		return resultMap;
	}
}
