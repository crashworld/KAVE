package com.leebrimelow.twitter.Search;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory; 

public class Search {
	/** ������ */
	private String[] args;
	/**������ ������ ��������������� ������� @link args*/
	private List<Tweet> tweets = null;
	/**	 */
	private Twitter twitter = null;
	
	Search(String[] args){
		
		twitter = new TwitterFactory().getInstance();
		
		setSearchArgs(args);
		setSearchedPeople();
	};
	
	/**������������� ���������� @link args ������� � �������� */
	private void setSearchArgs(String[] query)
	{
		args = query;
	}
	
	/**����������� ������ ������.
	  *	@return @link args */
	public String[] getSearchArgs()
	{
		return args;		
	}
	
	/**������������� ���������� @link tweets
	 *  ������� ���������������� ������ ������� @link args  */
	private void setSearchedPeople()
	{
		tweets = searchTweets(args);
	}
	
	/**	@return @link tweets */
	public List<Tweet> getSearchedPeople(){		
		return tweets;
	}
	
	/** ����� ������ �� ������� @link args
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
