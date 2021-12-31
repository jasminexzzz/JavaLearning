/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.property;

/**
 * <p>
 * This class holds current value of the config, and is responsible for informing all {@link PropertyListener}s
 * added on this when the config is updated.
 * 这个类保存配置的当前值，并负责在配置更新时通知所有添加到这个类上的{@link PropertyListener}。
 * </p>
 * <p>
 * Note that not every {@link #updateValue(Object newValue)} invocation should inform the listeners, only when
 * {@code newValue} is not Equals to the old value, informing is needed.
 * 注意，不是每个{@link #updateValue(Object newValue)}调用都应该通知监听器，只有当{@code newValue}不是等于旧值时，才需要通知。
 * </p>
 *
 * @param <T> the target type.
 * @author Carpenter Lee
 */
public interface SentinelProperty<T> {

    /**
     * <p>
     * Add a {@link PropertyListener} to this {@link SentinelProperty}. After the listener is added,
     * {@link #updateValue(Object)} will inform the listener if needed.
     * </p>
     * <p>
     * This method can invoke multi times to add more than one listeners.
     * </p>
     *
     * @param listener listener to add.
     */
    void addListener(PropertyListener<T> listener);

    /**
     * Remove the {@link PropertyListener} on this. After removing, {@link #updateValue(Object)}
     * will not inform the listener.
     *
     * @param listener the listener to remove.
     */
    void removeListener(PropertyListener<T> listener);

    /**
     * Update the {@code newValue} as the current value of this property and inform all {@link PropertyListener}s
     * added on this only when new {@code newValue} is not Equals to the old value.
     * 更新{@code newValue}作为这个属性的当前值，并且只有当new {@code newValue}不是旧值的Equals时，才通知所有添加到这个属性上的{@link PropertyListener}。
     *
     * @param newValue the new value.
     * @return true if the value in property has been updated, otherwise false
     */
    boolean updateValue(T newValue);
}
