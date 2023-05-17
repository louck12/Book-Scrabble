package model.server;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
