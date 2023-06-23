package logika;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListSets<E> implements DisjointSets<E> {

	private List<List<E>> sets;
	private int numSets;
	
	public ListSets() {
		sets = new LinkedList<List<E>>();
		numSets = 0;
	}
	
	@Override
	public void add (E e) {
		for (List<E> set : sets) {
			if (set.contains(e)) return;
		}
		LinkedList<E> b = new LinkedList<E> ();
		b.add(e);
		sets.add(b);
		numSets++;
	}
	
	@Override
	public E find (E e) {
		for (List<E> set : sets) {
			if (set.contains(e)) return set.get(0);
		}
		return null;
	}
	
	@Override
	public void union (E e1, E e2) {
		List<E> block1 = null, block2 = null;
		for (List<E> block : sets) {
			if (block.contains(e1)) block1 = block;
			if (block.contains(e2)) block2 = block;
		}
		if (block1 == null || block2 == null || block1 == block2) {
			//System.out.println("Ne gre narediti unije");
			if (block1 == null) System.out.println("block1 == null");
			if (block2 == null) System.out.println("block2 == null");
			return;
		}
		for (E e : block2) {
			block1.add(e);
		}
		sets.remove(block2);
		numSets--;
	}
		
	@Override
	public ListSets<E> deepCopy () {
		// Ustvari in vrne globoko kopijo danega objekta
		ListSets<E> kopija = new ListSets<E>();
		for (List<E> set : this.sets) {
			List<E> setVKopiji = new LinkedList<E>();
			for (E element : set) {
				setVKopiji.add(element);
			}
			kopija.sets.add(setVKopiji);
		}
		kopija.numSets = this.numSets;
		return kopija;
	}
	
	@Override 
	public int numSets() {
		return numSets;
	}

	@Override
	public Iterator<List<E>> iterator() {
		// Vrne iterator, ki se sprehodi čez vse disjunktne množice
		Iterator<List<E>> it = new Iterator<List<E>>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < numSets && sets.get(currentIndex) != null;
            }

            @Override
            public List<E> next() {
                return sets.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
	
	@Override
	public void remove(E e) {
		List<E> removedSet = null;
		for (List<E> set : sets) {
			if (set.contains(e)) {
				removedSet = set;
				break;
			}
		}
		sets.remove(removedSet);
		numSets--;
	}
	
	@Override
	public void addAll(Iterable<E> iter) {
		if (iter.iterator().hasNext()) {
			E prviElement = iter.iterator().next();
			this.add(prviElement);
			for (E e : iter) {
				this.add(e);
				this.union(prviElement, e);
			}
		}
	}
	
	@Override
	public String toString() {
		String str = "";
		for (List<E> set : sets)
			str += set.toString();
			str += "\n";
		return str;
	}
	
	@Override
	public void clear() {
		sets = new LinkedList<List<E>>();
		numSets = 0;
	}
	
}