/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package casadmin;
import java.util.*;

import javax.swing.AbstractListModel;


public class YPListModel extends AbstractListModel {
  Vector<String> model;

  public YPListModel() {
    model = new Vector();
  }

  public int getSize() {
    return model.size();
  }

  public Object getElementAt(int index) {
    return model.elementAt(index);
  }

  public void add(String element) {
    if (model.add(element)) {
      fireContentsChanged(this, 0, getSize());
    }
  }

  public void addAll(Vector<String> vector) {
    model.addAll(vector);
    fireContentsChanged(this, 0, getSize());
  }

  public void addAll(Object elements[]) {
    Collection<Object> c = Arrays.asList(elements);
    Iterator it = c.iterator();
    while (it.hasNext())
        model.add((String)it.next());
    fireContentsChanged(this, 0, getSize());
  }

  public void clear() {
    model.clear();
    fireContentsChanged(this, 0, getSize());
  }

  public boolean contains(String element) {
    return model.contains(element);
  }

  public Object firstElement() {
    return model.firstElement();
  }

  public Iterator iterator() {
    return model.iterator();
  }

  public Object lastElement() {
    return model.lastElement();
  }

  public boolean removeElement(String element) {
    boolean removed = model.remove(element);
    if (removed) {
      fireContentsChanged(this, 0, getSize());
    }
    return removed;
  }
}
