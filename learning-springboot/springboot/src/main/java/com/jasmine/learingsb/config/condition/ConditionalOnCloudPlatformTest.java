package com.jasmine.learingsb.config.condition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;

/**
 * @author : jasmineXz
 */
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
public class ConditionalOnCloudPlatformTest {
}
