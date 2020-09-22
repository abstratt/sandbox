package loft;

import java.util.HashSet;
import java.util.Set;

public class ShortestUniqueSubstring {
	public int solution(String S) {
		char[] array = S.toCharArray();
		for (int i = 1; i < array.length; i++) {
			if (!findUniqueSubstrings(array, i).isEmpty()) {
				return i;
			}
		}
		return S.length();
	}
	
	/**
	 * Finds all unique substrings with the given length.
	 * 
	 * @param string 
	 * @param length
	 * @return a set of all unique substrings with the given length found in the given string
	 */
	Set<String> findUniqueSubstrings(char[] string, int length) {
		Set<String> seen = new HashSet<>();
		Set<String> unique = new HashSet<>();
		for (int i = 0; i <= string.length - length; i++) {
			String candidate = new String(string, i, length);
			boolean isNew = seen.add(candidate);
			if (isNew) {
				unique.add(candidate);
			} else {
				unique.remove(candidate);
			}
		}
		return unique;
	}
}
