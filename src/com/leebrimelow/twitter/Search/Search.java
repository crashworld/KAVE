package com.leebrimelow.twitter.Search;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory; 

public class Search {
	/** Запрос */
	private String[] args;
	/**Список твитов удовлетворяющих запросу @link args*/
	private List<Tweet> tweets = null;
	/**	 */
	private Twitter twitter = null;
	
	Search(String[] args){
		
		twitter = new TwitterFactory().getInstance();
		
		setSearchArgs(args);
		setSearchedPeople();
	};
	
	/**Инициализация переменной @link args строкой с запросом */
	private void setSearchArgs(String[] query)
	{
		args = query;
	}
	
	/**Возвращение строки поиска.
	  *	@return @link args */
	public String[] getSearchArgs()
	{
		return args;		
	}
	
	/**Инициализация переменной @link tweets
	 *  Твитами удовлетворяющими строке запроса @link args  */
	private void setSearchedPeople()
	{
		tweets = searchTweets(args);
	}
	
	/**	@return @link tweets */
	public List<Tweet> getSearchedPeople(){		
		return tweets;
	}
	
	/** Поиск Твитов по запросу @link args
	 * @return 	List<Tweet> */
	private List<Tweet> searchTweets(String[] args){
		
		QueryResult result = null;
		
		if (args.length > 0) {			
			try {
				 result = twitter.search(new Query(args[0]));
			} catch (TwitterException te) {
				te.printStackTrace();        
		 		} 
		}
		
		return result.getTweets();	
	}
}
