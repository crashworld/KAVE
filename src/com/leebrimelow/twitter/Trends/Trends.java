package com.leebrimelow.twitter.Trends;

import java.util.HashMap;
import java.util.Map;

import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class Trends {
	/**Переменная, содержащая пары ключ-значение( имя тренда и  Url тренда )*/
	private Map<String,String> mapOfTrends = null;	
	private Twitter twitter = null;
		
	Trends(){
		
		mapOfTrends = new HashMap<String,String>();
		twitter = new TwitterFactory().getInstance();
		
		setTrendsMap();
	}
	/**@return Возвращает кол-во трендов */
	public final int countTrends()
	{
		return mapOfTrends.size();
	}
	
	/** Заполнение трендами переменной @link mapOfTrends*/
	private void setTrendsMap(){
		for(Trend trend : getTrends())
		{
			mapOfTrends.put(trend.getName(), trend.getUrl());
		}
	}
	
	/** */
	public Map<String,String> getTrendsMap(){
		return mapOfTrends;		
	}	
	
	/** @return Trend[] */
	private Trend[] getTrends(){
		Trends trends = null;
		
		try{			 
			trends =  (Trends) twitter.getTrends();	
			} catch (TwitterException te) {
				te.printStackTrace();				
				}
		
		return trends.getTrends();		
	}	

	
}
