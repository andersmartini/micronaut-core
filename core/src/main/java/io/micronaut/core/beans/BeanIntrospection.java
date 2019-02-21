/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.core.beans;

import io.micronaut.core.annotation.AnnotationMetadataDelegate;
import io.micronaut.core.reflect.exception.InstantiationException;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.ArgumentUtils;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Optional;

/**
 * A {@link BeanIntrospection} is the result of compile time computation of a beans properties and annotation metadata.
 *
 * @param <T> The bean type
 * @author graemerocher
 * @since 1.1
 */
public interface BeanIntrospection<T> extends AnnotationMetadataDelegate {

    /**
     * @return A immutable collection of properties.
     */
    @Nonnull Collection<BeanProperty<T, Object>> getBeanProperties();

    /**
     * Get all the bean properties annotated for the given type.
     *
     * @param annotationType The annotation type
     * @return A immutable collection of properties.
     */
    @Nonnull Collection<BeanProperty<T, Object>> getBeanProperties(Class<? extends Annotation> annotationType);

    /**
     * Instantiates an instance of the bean, throwing an exception is instantiation is not possible.
     *
     * @return An instance
     * @throws InstantiationException If the bean cannot be insantiated.
     */
    @Nonnull T instantiate() throws InstantiationException;

    /**
     * Instantiates an instance of the bean, throwing an exception is instantiation is not possible.
     *
     * @param arguments The arguments required to instantiate bean. Should match the types returned by {@link #getConstructorArguments()}
     * @return An instance
     * @throws InstantiationException If the bean cannot be instantiated.
     */
    @Nonnull T instantiate(Object... arguments) throws InstantiationException;

    /**
     * The bean type.
     * @return The bean type
     */
    @Nonnull Class<T> getBeanType();

    /**
     * The constructor arguments needed to instantiate the bean.
     * @return An argument array
     */
    default @Nonnull Argument<?>[] getConstructorArguments() {
        return Argument.ZERO_ARGUMENTS;
    }

    /**
     * Obtain a property by name.
     * @param name The name of the property
     * @return A bean property if found
     */
    default @Nonnull Optional<BeanProperty<T, Object>> getProperty(@Nonnull String name) {
        return Optional.empty();
    }

    /**
     * Obtain a property by name and type.
     * @param name The name of the property
     * @param type The property type to search for
     * @return A bean property if found
     * @param <P> The property type
     */
    default @Nonnull <P> Optional<BeanProperty<T, P>> getProperty(@Nonnull String name, @Nonnull Class<P> type) {
        ArgumentUtils.requireNonNull("name", name);
        ArgumentUtils.requireNonNull("type", type);

        final BeanProperty<T, ?> prop = getProperty(name).orElse(null);
        if (prop != null && type.isAssignableFrom(prop.getType())) {
            //noinspection unchecked
            return Optional.of((BeanProperty<T, P>) prop);
        }
        return Optional.empty();
    }

    /**
     * The property names as an array.
     *
     * @return The properties names
     */
    default @Nonnull String[] getPropertyNames() {
        return getBeanProperties().stream().map(BeanProperty::getName).toArray(String[]::new);
    }
}
