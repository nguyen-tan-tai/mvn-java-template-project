package framework.util;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

/**
 * ArrayList with extra functions
 *
 * @param <T>
 */
public class ArrayListExtend<T> extends ArrayList<T> {
  private static final long serialVersionUID = 1L;

  /**
   * Add item to list and return the list itself
   *
   * @param t
   * @return ArrayListExtend<T>
   */
  public ArrayListExtend<T> addItem(@NonNull T t) {
    this.add(t);
    return this;
  }

  /**
   * Add item to list and return the list itself
   *
   * @param list
   * @return ArrayListExtend<T>
   */
  public ArrayListExtend<T> addItems(@NonNull List<T> list) {
    this.addAll(list);
    return this;
  }
}
