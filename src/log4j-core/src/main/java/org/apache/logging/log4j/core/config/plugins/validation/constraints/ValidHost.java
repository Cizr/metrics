/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.log4j.core.config.plugins.validation.constraints;

<<<<<<< HEAD
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
=======
import java.lang.annotation.*;
>>>>>>> 1ead477e44ef3058b5f58f3f62dcf08366b87f1c
import java.net.InetAddress;
import org.apache.logging.log4j.core.config.plugins.validation.Constraint;
import org.apache.logging.log4j.core.config.plugins.validation.validators.ValidHostValidator;

/**
 * Indicates that a plugin attribute must be a valid host. This relies on the same validation rules as
 * {@link InetAddress#getByName(String)}.
 *
 * @since 2.8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(ValidHostValidator.class)
public @interface ValidHost {

    /**
     * The message to be logged if this constraint is violated. This should normally be overridden.
     */
    String message() default "The hostname is invalid";
}