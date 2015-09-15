package com.hea3ven.transition.f.common.property;

public interface IUnlistedProperty<T> {

	String getName();

	boolean isValid(T value);

	Class<T> getType();

	String valueToString(T value);

}
