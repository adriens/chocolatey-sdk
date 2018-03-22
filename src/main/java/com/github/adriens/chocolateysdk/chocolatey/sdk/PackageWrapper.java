/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.chocolateysdk.chocolatey.sdk;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author salad74
 */
public class PackageWrapper {

    /**
     * @return the nuspecPackageMetaData
     */
    public NuspecPackageMetaData getNuspecPackageMetaData() {
        return nuspecPackageMetaData;
    }

    /**
     * @param nuspecPackageMetaData the nuspecPackageMetaData to set
     */
    public void setNuspecPackageMetaData(NuspecPackageMetaData nuspecPackageMetaData) {
        this.nuspecPackageMetaData = nuspecPackageMetaData;
    }
    public void setNuspecPackageMetaData() throws Exception{
        NuspecParser parser = new NuspecParser(getPackageName(), getPackageVersion());
        this.nuspecPackageMetaData = parser.getNuspecPackageMetaData();
    }
    

    /**
     * @return the downloadURL
     */
    public URL getDownloadURL() {
        return downloadURL;
    }

    /**
     * @param downloadURL the downloadURL to set
     */
    public void setDownloadURL(URL downloadURL) {
        this.downloadURL = downloadURL;
    }

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the packageVersion
     */
    public String getPackageVersion() {
        return packageVersion;
    }

    /**
     * @param packageVersion the packageVersion to set
     */
    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    /**
     * @return the nbCurrentVersionDownloads
     */
    public int getNbCurrentVersionDownloads() {
        return nbCurrentVersionDownloads;
    }

    /**
     * @param nbCurrentVersionDownloads the nbCurrentVersionDownloads to set
     */
    public void setNbCurrentVersionDownloads(int nbCurrentVersionDownloads) {
        this.nbCurrentVersionDownloads = nbCurrentVersionDownloads;
    }

    /**
     * @return the nbAllVersionsDownloads
     */
    public int getNbAllVersionsDownloads() {
        return nbAllVersionsDownloads;
    }

    /**
     * @param nbAllVersionsDownloads the nbAllVersionsDownloads to set
     */
    public void setNbAllVersionsDownloads(int nbAllVersionsDownloads) {
        this.nbAllVersionsDownloads = nbAllVersionsDownloads;
    }

    /**
     * @return the lastUpdate
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    final static Logger logger = LoggerFactory.getLogger(PackageWrapper.class);
    
    private String packageName;
    private String packageVersion;
    private int nbCurrentVersionDownloads;
    private int nbAllVersionsDownloads;
    private Date lastUpdate;
    private URL downloadURL;
    private NuspecPackageMetaData nuspecPackageMetaData;
    private URL nupkgDownloadURL;
    
    
    public static final String URL_PACKAGES_ROOT = "https://packages.chocolatey.org/";
    
    /**
     * @return the nupkgDownloadURL
     */
    public URL getNupkgDownloadURL() {
        return nupkgDownloadURL;
    }

    /**
     * @param nupkgDownloadURL the nupkgDownloadURL to set
     */
    public void setNupkgDownloadURL(URL nupkgDownloadURL) {
        this.nupkgDownloadURL = nupkgDownloadURL;
    }
    
    
    public PackageWrapper(String aPackageName) throws Exception{
        this.packageName = aPackageName;
        this.packageName = this.packageName.trim();
        try{
            logger.info("Trying to get datas from the web for package <" + this.packageName+ ">...");
            fillUp();
            logger.info("Successfuly got datas from the web for package <" + getPackageName() + ">.");
        }
        catch(Exception ex){
            logger.error("Was not able to fetch datas for package <" + this.packageName + "> : " + ex.getMessage() );
            throw ex;
        }
    }
    
    
    
    public PackageWrapper(String aPackageName, boolean fetchNuspec) throws Exception{
        this.packageName = aPackageName;
        this.packageName = this.packageName.trim();
        try{
            logger.info("Trying to get datas from the web for package <" + this.packageName+ ">...");
            fillUp();
            logger.info("Successfuly got datas from the web for package <" + getPackageName() + ">.");
            
            if(fetchNuspec){
                logger.info("Now getting datas from online nuspec file...");
                setNuspecPackageMetaData();
                logger.info("Sucessfully got nuspec datas.");
            }
            else{
                logger.info("Will NOT fetch metadatas from nuspec.");
            }
            
        }
        catch(Exception ex){
            logger.error("Was not able to fetch datas for package <" + this.packageName + "> : " + ex.getMessage() );
            throw ex;
        }
    }
    
    public PackageWrapper(){
        
    }
    
    
    public final static URL composeNuPkgDownloadURL(String aPackage, String aVersion)
            throws MalformedURLException {
        URL out;
        try{
            if(aPackage == null | aVersion == null){
            return null;
        } else{
            return new URL(PackageWrapper.URL_PACKAGES_ROOT + aPackage + "." + aVersion + ".nupkg");//https://packages.chocolatey.org/schemacrawler.14.20.03.nupkg
        }
        }
        catch(MalformedURLException ex){
            logger.error("Was not able to build a proper URL for the nuspec file : " + ex.getMessage());
            throw ex;
        }
        
    }
    private static WebClient buildWebClient() {
        // Disable verbose logs
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        
        return webClient;
    }
    
    public void fillUp() throws IOException{
        WebClient webClient = buildWebClient();
        HtmlPage htmlPage = webClient.getPage("https://chocolatey.org/packages/" + this.getPackageName());
        
        // get version
        // /html/body/div[1]/div/div[2]/div[1]/div[2]/h2[1]
        HtmlElement versionElement = htmlPage.getFirstByXPath("/html/body/div[1]/div/div[2]/div[1]/div[2]/h2[1]");
        this.setPackageVersion(versionElement.getTextContent());
        logger.info("Found version for package <" + this.getPackageName() + "> : <" + this.getPackageVersion() + ">" );
        
    
        // set nupkgDownloadURL
        URL nuPkgUrl = PackageWrapper.composeNuPkgDownloadURL(getPackageName(), getPackageVersion());
        setNupkgDownloadURL(nuPkgUrl);
        logger.info("Setup nupkg download url : <" + getNupkgDownloadURL() + ">");
        
        // nb downloads of current version
        HtmlElement downloadsElement = htmlPage.getFirstByXPath("/html/body/div[1]/div/div[1]/div/div[2]/p[1]");
        //String tmpDownloads = downloadsElement.getTextContent();
        //tmpDownloads = tmpDownloads.replaceAll(",", "");
        //logger.info("tmpDownload : <" + tmpDownloads + ">");
        this.setNbCurrentVersionDownloads(Integer.parseInt(downloadsElement.getTextContent().replaceAll(",", "")));
        
        logger.info("Nb downloads of current version :  <" + this.getNbCurrentVersionDownloads() + ">");
        
        
        // nb download since the first version
        HtmlElement allDownloadsElement = htmlPage.getFirstByXPath("/html/body/div[1]/div/div[1]/div/div[1]/p[1]");
        logger.debug("Nb downloads of all versions (raw Text from website) :  <" + allDownloadsElement.getTextContent() + ">");
        this.setNbAllVersionsDownloads(Integer.parseInt(allDownloadsElement.getTextContent().replaceAll(",", "")));
        logger.info("Nb downloads of all versions :  <" + this.getNbAllVersionsDownloads() + ">");
        
        
        
        // 
        //last update time
        HtmlElement lastUpdateElement = htmlPage.getFirstByXPath("/html/body/div[1]/div/div[1]/div/div[3]/p[1]");
        logger.info("Last Update raw content :  <" + lastUpdateElement.getTextContent() + ">");
        // let's parse the date
        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
        try{
            Date updatedate = sourceFormat.parse(lastUpdateElement.getTextContent());
            logger.info("Found update date : " + updatedate);
            this.setLastUpdate(updatedate);
        }
        catch(ParseException ex){
            logger.error("Was not able to parse input date <" + lastUpdateElement.getTextContent() +"> : " + ex.getMessage());
            logger.error("Last Update Date set to null");
        }
        
        // download URL
        // here we cannot use xpath as the link position is changing
        HtmlAnchor downloadURLElement = htmlPage.getAnchorByText("Download");
        
        //String rawDownloadURL = downloadURLElement.get ;
        logger.debug("Found download URL : <" + downloadURLElement.getHrefAttribute() + ">");
        setDownloadURL(new URL(downloadURLElement.getHrefAttribute()));
        logger.info("Download URL : <" + getDownloadURL() + ">");
 
        
        /*
        // dependances
        DomElement domDeps;
        domDeps = htmlPage.getFirstByXPath("/html/body/div[1]/div/div[2]/div[1]/ul[2]/li/ul");
        //logger.info(domDeps.getTextContent());
        Iterator<DomElement> depsIter = domDeps.getChildElements().iterator();
        DomElement lDep;
        while(depsIter.hasNext()){
            lDep = depsIter.next();
            //logger.info(lDep.asText());
            logger.info("url : <" + lDep.getElementsByTagName("a").get(0).getAttribute("href") + ">");
            
            logger.info("package name: <" + lDep.getElementsByTagName("a").get(0).asText() + ">");
            
            // the version
            //logger.info("dep version : <" + lDep.getElementsByTagName("span").get(0).asText() + ">");
            
        }*/
        
        
    }
    
    public String toString(){
        String out = "<Package/Version> : <" + getPackageName() + "/" + getPackageVersion() +">";
        return out;
    }
    public static void main(String[] args) {
        try {
            PackageWrapper wrap = new PackageWrapper("schemacrawler", true);
            logger.info("Found package : " + wrap);
            //wrap.fetchCrawlPackage("schemacrawler");
            //wrap.fetchCrawlPackage("liquibase");
            //wrap.fetchCrawlPackage("Firefox");
            //wrap.fetchCrawlPackage("Graphviz");
            System.exit(0);
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
