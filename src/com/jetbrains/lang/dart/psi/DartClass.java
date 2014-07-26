package com.jetbrains.lang.dart.psi;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DartClass extends DartComponent
{
	@Nullable
	DartType getSuperClass();

	@NotNull
	List<DartType> getImplementsList();

	@NotNull
	List<DartType> getMixinsList();

	boolean isGeneric();

	@NotNull
	List<DartComponent> getMethods();

	@NotNull
	List<DartComponent> getFields();

	@NotNull
	List<DartComponent> getConstructors();

	@Nullable
	DartComponent findFieldByName(@NotNull final String name);

	@Nullable
	DartComponent findMethodByName(@NotNull final String name);

	@Nullable
	DartComponent findMemberByName(@NotNull final String name);

	@NotNull
	List<DartComponent> findMembersByName(@NotNull final String name);

	@Nullable
	DartTypeParameters getTypeParameters();

	@Nullable
	DartOperator findOperator(String operator, @Nullable DartClass rightDartClass);

	List<DartOperator> getOperators();

	@Nullable
	DartComponent findNamedConstructor(String name);
}
