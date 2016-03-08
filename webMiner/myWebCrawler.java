package myWebCrawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
* The Web Mining progrm using Jsoup Parser ,
* to find contact details like Tel/Phone/fax numbers and email addresses
* along with Social websites Link like FB, Twitter, LinkedIn etc.
* This program can be used as bot or crawler for mining purpose.
*
* @author  Prasad Belhe
* @version 1.0
* @since   2016-03-08
**/


public class myWebCrawler {
	//update public-private set-get
	private String url;
	private Document doc;
	private Matcher mymacher;
	private Pattern REmail = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
	private Pattern REtel = Pattern.compile("(\\w{3,8}:)\\s?\\+?\\d{0,4}[\\s.-]*(\\(?\\d{0,4}\\)?)?[\\s.-]*\\d{3,4}[\\s.-]*\\d{3,4}");
	public HashSet<String> urls = new HashSet<String>();
	public HashSet<String> fb = new HashSet<String>();
	public HashSet<String> ln = new HashSet<String>();
	public HashSet<String> tw = new HashSet<String>();
	public HashSet<String> g = new HashSet<String>();
	public HashSet<String> yt = new HashSet<String>();
	public HashSet<String> phone = new HashSet<String>();
	public HashSet<String> mail = new HashSet<String>();
	private HashMap<String, HashSet<String>> social = new HashMap<String, HashSet<String>>();
	private HashMap<String, HashSet<String>> contact = new HashMap<String, HashSet<String>>();

	public static void main(String[] args) {
		//myWebCrawler c = new myWebCrawler("http://www.easyjet.com/en/help/contact-us/telephone");	
		//myWebCrawler c = new myWebCrawler("http://www.oracle.com/in/corporate/contact/index.html");
		myWebCrawler c ;
		c= new myWebCrawler("http://www.baldor.com/contact/contact-us/general-inquiry/");
		c.myFinder();
		System.out.println("All links & text :"+c.urls);
		System.out.println("All visible text :"+c.doc.text());
		System.out.println("Social Links :"+c.getSocial());
		System.out.println("contact Informations :"+c.getContact());		
	}
	
	public myWebCrawler() { }//default constructor
	public myWebCrawler(String url) { this.url=url;	}
	
	private void myFinder() {
		visit();
		Contact();
		Social();
		URLs();		
	}	
	private void visit() {
		try {
			doc = Jsoup.connect(url).userAgent("firefox").timeout(1*60000).get();
		} catch (IOException e) {
			System.err.println("Cant visit :"+e);
		}		
	}
	
	private void Social() {
		Elements links = doc.select("a[href]");		
		for (Element link : links) {
			String target=link.attr("abs:href").toLowerCase();
			if(target.contains("facebook"))			fb.add(target);
			else if(target.contains("linkedin"))	ln.add(target);
			else if(target.contains("twitter"))		tw.add(target);
			else if(target.contains("google"))		g.add(target);
			else if(target.contains("youtube"))		yt.add(target);
		}
		if(fb.size()>0) social.put("facebook", fb);
		if(ln.size()>0)social.put("linkedin",ln );
		if(tw.size()>0)social.put("twitter", tw);
		if(g.size()>0)social.put("google", g);
		if(yt.size()>0)social.put("youtube",yt );		
	}

	private void Contact() {
		mymacher = REmail.matcher(doc.toString());
		while(mymacher.find()) mail.add(doc.toString().substring( mymacher.start(), mymacher.end()));
		mymacher = REtel.matcher(doc.toString().toLowerCase());
		while(mymacher.find()) phone.add(doc.toString().substring( mymacher.start(), mymacher.end()));
		if(phone.size()>0)contact.put("Phone", phone );
		if(mail.size()>0)contact.put("Mail", mail );
	}
	
	private void URLs() {
		Elements links = doc.select("a[href]");		
		for (Element link : links) 	urls.add(link.attr("abs:href"));//System.out.println("Hypertext :"+link.text());
	}

	private HashMap<String,HashSet<String>> getSocial() { return social; }
	private HashMap<String,HashSet<String>> getContact() { return contact;	}
	
}
