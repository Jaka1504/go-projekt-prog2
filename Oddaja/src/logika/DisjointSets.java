package logika;

import java.util.List;

public interface DisjointSets<E> extends Iterable<List<E>>{
	
	public void add(E e);
	
	public E find(E e);
	
	public void union(E e1, E e2);
	
	public int numSets();
	
	public DisjointSets<E> deepCopy();

	public void remove(E e);
	
	public void addAll(Iterable<E> iter);
	
	public void clear();

}
