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

package io.micronaut.inject.beans;

import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.UsedByGeneratedCode;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.core.util.ArgumentUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Abstract implementation of the {@link BeanIntrospection} interface.
 *
 * @param <T> The generic type
 * @author graemerocher
 * @since 1.1
 */
@UsedByGeneratedCode
@Internal
public abstract class AbstractBeanIntrospection<T> implements BeanIntrospection<T> {

    protected final AnnotationMetadata annotationMetadata;
    protected final Class<T> beanType;
    @SuppressWarnings("WeakerAccess")
    protected final BeanProperty<T, Object>[] beanProperties;

    // used for indexed properties
    private Map<Class<? extends Annotation>, List<BeanProperty<T, Object>>> indexed;

    /**
     * Base class for bean instrospections.
     * @param beanType The bean type
     * @param annotationMetadata The annotation metadata
     * @param beanProperties The bean prooperties
     */
    @UsedByGeneratedCode
    protected AbstractBeanIntrospection(
            @Nonnull Class<T> beanType,
            @Nullable AnnotationMetadata annotationMetadata,
            @Nullable BeanProperty<T, Object>[] beanProperties) {
        ArgumentUtils.requireNonNull("beanType", beanType);
        this.beanType = beanType;
        this.annotationMetadata = annotationMetadata == null ? AnnotationMetadata.EMPTY_METADATA : annotationMetadata;
        //noinspection unchecked
        this.beanProperties = beanProperties == null ? new BeanProperty[0] : beanProperties;
    }

    @Nonnull
    @Override
    public List<BeanProperty<T, Object>> getProperties(Class<? extends Annotation> annotationType) {
        return Collections.emptyList();
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return annotationMetadata;
    }

    @Nonnull
    @Override
    public List<BeanProperty<T, Object>> getProperties() {
        return Arrays.asList(beanProperties);
    }

    @Nonnull
    @Override
    public Class<T> getBeanType() {
        return beanType;
    }

    /**
     * Adds a property at a particular index of the internal array passed to the constructor. Used by
     * generated byte code for subclasses and not for public consumption.
     *
     * @param index The index of the property
     * @param property The property.
     */
    @SuppressWarnings("unused")
    @Internal
    @UsedByGeneratedCode
    protected final void addProperty(int index, @Nonnull BeanProperty<T, Object> property) {
        ArgumentUtils.requireNonNull("property", property);
        if (index >= beanProperties.length) {
            throw new IllegalStateException("Invalid byte code generated during bean introspection");
        }
        beanProperties[index] = property;
    }

    /**
     * Used to produce an index for particular annotation type. Method referenced by generated byte code and
     * not for public consumption.
     *
     * @param annotationType The annotation type
     * @param property The property.
     */
    @SuppressWarnings("unused")
    @Internal
    @UsedByGeneratedCode
    protected final void indexProperty(Class<? extends Annotation> annotationType, @Nonnull BeanProperty<T, Object> property) {
        ArgumentUtils.requireNonNull("property", property);
        if (annotationType != null) {
            if (indexed == null) {
                indexed = new HashMap<>(3);
            }
            final List<BeanProperty<T, Object>> indexed = this.indexed.computeIfAbsent(annotationType, aClass -> new ArrayList<>(3));

            indexed.add(property);
        }
    }
}
