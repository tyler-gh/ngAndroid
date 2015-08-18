/**
 * Copyright (C) 2010-2015 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.davityle.ngprocessor.util;

/**
 * I'd love to use Guava's Optional, but we're trying to keep the dependency
 * level to a minimum.
 */
public class Option<T> {

	private static final Option<?> ABSENT = new Option<Object>(null, false);

	public static <T> Option<T> of(T value) {
		return new Option<>(value, value != null);
	}

	@SuppressWarnings("unchecked")
	public static <T> Option<T> absent() {
		return (Option<T>) ABSENT;
	}

	private final T reference;

	private final boolean isPresent;

	private Option(T reference, boolean isPresent) {
		this.reference = reference;
		this.isPresent = isPresent;
	}

	public boolean isPresent() {
		return isPresent;
	}

	public boolean isAbsent() {
		return !isPresent;
	}

	public T get() {
		if (!isPresent) {
			throw new IllegalStateException("value is absent");
		}
		return reference;
	}

	public <R> R fold(OptionCB<T,R> cb){
		if(isPresent){
			return cb.present(reference);
		} else {
			return cb.absent();
		}
	}

	@Override
	public String toString() {
		return isPresent() ? get().toString() : "absent";
	}

	public T getOrElse(T no_id) {
		return isPresent() ? get() : no_id;
	}

	public <R> Option<R> map(Map<T, R> map) {
		return isPresent() ? Option.of(map.map(get())) : Option.<R>absent();
	}

	public interface Map<T, R> {
		R map(T t);
	}

	public interface OptionCB<T, R> {
		R absent();
		R present(T t);
	}
}
