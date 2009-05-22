package org.meta_environment.rascal.ast;

public class MappingCache<K, V> {
	private final static int DEFAULT_INITIAL_LOG_SIZE = 10;

	private int hashMask;

	private Entry<K, V>[] data;

	private int threshold;

	private int load;

	public MappingCache() {
		super();

		int tableSize = 1 << DEFAULT_INITIAL_LOG_SIZE;
		hashMask = tableSize - 1;
		data = new Entry[tableSize];

		threshold = tableSize;

		load = 0;
	}

	public MappingCache(int initialLogSize) {
		super();

		int tableSize = 1 << initialLogSize;
		hashMask = tableSize - 1;
		data = new Entry[tableSize];

		threshold = tableSize;

		load = 0;
	}

	public MappingCache(int initialLogSize, float loadFactor) {
		super();

		int tableSize = 1 << initialLogSize;
		hashMask = tableSize - 1;
		data = new Entry[tableSize];

		threshold = (int) (tableSize * loadFactor);

		load = 0;
	}

	private void rehash() {
		Entry<K, V>[] oldData = data;

		int tableSize = data.length << 1;
		int newHashMask = tableSize - 1;
		Entry<K, V>[] newData = new Entry[tableSize];

		// Construct temporary entries that function as roots for the entries
		// that remain in the current bucket
		// and those that are being shifted.
		Entry<K, V> currentEntryRoot = new Entry<K, V>(0, null, null, null);
		Entry<K, V> shiftedEntryRoot = new Entry<K, V>(0, null, null, null);

		int newLoad = load;
		int oldSize = oldData.length;
		for (int i = oldSize - 1; i >= 0; i--) {
			Entry<K, V> e = oldData[i];
			if (e != null) {
				Entry<K, V> lastCurrentEntry = currentEntryRoot;
				Entry<K, V> lastShiftedEntry = shiftedEntryRoot;
				do {
					int position = e.hash & newHashMask;

					if (position == i) {
						lastCurrentEntry.next = e;
						lastCurrentEntry = e;
					} else {
						lastShiftedEntry.next = e;
						lastShiftedEntry = e;
					}

					e = e.next;
				} while (e != null);

				// Set the next pointers of the last entries in the buckets to
				// null.
				lastCurrentEntry.next = null;
				lastShiftedEntry.next = null;

				newData[i] = currentEntryRoot.next;
				newData[i | oldSize] = shiftedEntryRoot.next; // The entries got
				// shifted by
				// the size of
				// the old
				// table.
			}
		}

		load = newLoad;

		threshold <<= 1;
		data = newData;
		hashMask = newHashMask;
	}

	private void ensureCapacity() {
		if (load > threshold) {
			rehash();
		}
	}

	public V store(K key, V value) {
		int hash = key.hashCode();
		int position = hash & hashMask;

		// Check if the key is already in here.
		Entry<K, V> startEntry = data[position];
		if (startEntry != null) {
			Entry<K, V> entry = startEntry;
			do {
				if (hash == entry.hash && key.equals(entry.key))
					return entry.value; // The key is already present.

				entry = entry.next;
			} while (entry != null);
		}

		// Couldn't find the key.
		ensureCapacity();

		data[position] = new Entry<K, V>(hash, key, value, startEntry); // Insert
		// the
		// new
		// entry.

		load++;

		return value;
	}

	public V get(K key) {
		int hash = key.hashCode();
		int position = hash & hashMask;

		Entry<K, V> entry = data[position];
		while (entry != null) {
			if (hash == entry.hash && key.equals(entry.key))
				return entry.value;

			entry = entry.next;
		}

		return null;
	}

	public void putUnsafe(K key, V value) {
		int hash = key.hashCode();
		int position = hash & hashMask;

		ensureCapacity();

		data[position] = new Entry<K, V>(hash, key, value, data[position]);

		load++;
	}

	public int size() {
		return load;
	}

	private static class Entry<K, V> {
		public final int hash;
		public final K key;
		public final V value;

		public Entry<K, V> next;

		public Entry(int hash, K key, V value, Entry<K, V> next) {
			super();

			this.hash = hash;
			this.key = key;
			this.value = value;

			this.next = next;
		}
	}
}