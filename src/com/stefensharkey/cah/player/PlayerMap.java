package com.stefensharkey.cah.player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PlayerMap<K, V> extends LinkedHashMap<K, V>
{
	private static final long serialVersionUID = -6269705914333333985L;

	public V getValue(int num)
	{
		Map.Entry<K, V> entry = getEntry(num);
		if (entry == null)
			return null;
		return entry.getValue();
	}

	public Map.Entry<K, V> getEntry(int i)
	{
		Set<Map.Entry<K, V>> entries = entrySet();
		int x = 0;

		for (Map.Entry<K, V> entry : entries)
			if (x++ == i)
				return entry;
		return null;
	}
}
