package com.github.rutledgepaulv.basic.qbuilders.conditions;

import com.github.rutledgepaulv.basic.qbuilders.properties.concrete.basic.*;

import java.util.List;

/**
 * Represents the starting point of a logical condition. It can be brought to
 * completion by specifying a field and specifying a constraint against that field,
 * or by logically composing conditions into a new logically complete condition.
 *
 * @param <T>
 */
public interface Partial<T extends Partial<T>> {

    /**
     * For usage when the field is known to contain values of a boolean type.
     *
     * @param field The name of the field.
     *
     * @return The property interface so that a constraint can be set against the field.
     */
    BooleanProperty<T> bool(String field);

    /**
     * For usage when the field is known to contain values of a string type.
     *
     * @param field The name of the field.
     *
     * @return The property interface so that a constraint can be set against the field.
     */
    StringProperty<T> string(String field);

    /**
     * For usage when the field is known to contain values of a long type.
     *
     * @param field The name of the field.
     *
     * @return The property interface so that a constraint can be set against the field.
     */
    LongProperty<T> longNum(String field);

    /**
     * For usage when the field is known to contain values of an integer type.
     *
     * @param field The name of the field.
     *
     * @return The property interface so that a constraint can be set against the field.
     */
    IntegerProperty<T> intNum(String field);

    /**
     * For usage when the field is known to contain values of a numerical short type.
     *
     * @param field The name of the field.
     *
     * @return The property interface so that a constraint can be set against the field.
     */
    ShortProperty<T> shortNum(String field);

    /**
     * For usage when the field is known to contain values of a numerical float type.
     *
     * @param field The name of the field.
     *
     * @return The property interface so that a constraint can be set against the field.
     */
    FloatProperty<T> floatNum(String field);

    /**
     * For usage when the field is known to contain values of a numerical double type.
     *
     * @param field The name of the field.
     *
     * @return The property interface so that a constraint can be set against the field.
     */
    DoubleProperty<T> doubleNum(String field);

    /**
     * For usage when the field is known to contain values of a point-in-time type.
     *
     * @param field The name of the field.
     *
     * @return The property interface so that a constraint can be set against the field.
     */
    InstantProperty<T> instant(String field);

    /**
     * Allows for composing a list of conditions in a "any match" fashion.
     *
     * @param conditions The list of conditions to combine.
     *
     * @return The logical condition that represents the composition of the list.
     */
    Condition<T> or(List<Condition<T>> conditions);

    /**
     * Allows for composing a list of conditions in a "all match" fashion.
     *
     * @param conditions The list of conditions to combine.
     *
     * @return The logical condition that represents the composition of the list.
     */
    Condition<T> and(List<Condition<T>> conditions);

    /**
     * Allows for composing a list of conditions in a "any match" fashion.
     *
     * @param c1 The first condition to combine.
     * @param c2 The second condition to combine.
     * @param cn Any other conditions to combine.
     *
     * @return The logical condition that represents the composition of the list.
     */
    Condition<T> or(Condition<T> c1, Condition<T> c2, Condition<T>... cn);

    /**
     * Allows for composing a list of conditions in a "all match" fashion.
     *
     * @param c1 The first condition to combine.
     * @param c2 The second condition to combine.
     * @param cn Any other conditions to combine.
     *
     * @return The logical condition that represents the composition of the list.
     */
    Condition<T> and(Condition<T> c1, Condition<T> c2, Condition<T>... cn);

}
