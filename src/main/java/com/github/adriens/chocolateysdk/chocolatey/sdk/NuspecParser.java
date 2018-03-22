/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.chocolateysdk.chocolatey.sdk;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author salad74
 */
public class NuspecParser {

    final static Logger logger = LoggerFactory.getLogger(NuspecParser.class);
    
    private NuspecPackageMetaData nuspecPackageMetaData;
    
    private String packageName;
    private String version;
    
    
    public NuspecParser(){
        
    }
    
    public NuspecParser(String aPackage, String aVersion) throws Exception{
        setPackageName(aPackage);
        setVersion(aVersion);
        logger.info("Nuspec parser built with <package/version> : <" + getPackageName() + "/" + getVersion() + ">" );
        setUpNuspecPackageMetaData();
    }
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
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    private void setUpNuspecPackageMetaData()
            throws FileSystemException, MalformedURLException,
            SAXException, IOException,
            ParserConfigurationException, XPathExpressionException {
        try{
            this.nuspecPackageMetaData = new NuspecPackageMetaData();
            
            FileSystemManager fsManager = VFS.getManager();
            URL nuPkgUrl = PackageWrapper.composeNuPkgDownloadURL(getPackageName(), getVersion());
            
            
            FileObject nuPkgFile = fsManager.resolveFile("zip:" + nuPkgUrl.toString());
            // now get the nuspec file
            // List the children of the Jar file
            FileObject[] children = nuPkgFile.getChildren();
            logger.debug("Children of " + nuPkgFile.getName().getURI());
            FileObject nuspecFile = null;
            for (int i = 0; i < children.length; i++) {
                logger.info(children[i].getName().getBaseName());
                //get the main (one and only one) package nupec
                if (children[i].getName().getBaseName().endsWith(".nuspec")) {
                    nuspecFile = children[i];
                    logger.info("Found nuspec file : <" + nuspecFile.getName() + ">");
                    break;
                }
            }
            // now we got the nuspec fileObject
            // next, build the DOM
            // build the dom

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(nuspecFile.getContent().getInputStream());
            logger.debug("Successfuly loaded xml");
            doc.getDocumentElement().normalize();
            logger.debug("nuspec filen normalized.");
            
            // we got the nuspec properly loaded.
            ///////////////////////////////////////////////////////
            // get nodes
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            // packageId
            String expression = "//package/metadata/id";
            Node node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String packageId = node.getTextContent();
            logger.debug("Found node id : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setId(packageId);
            
            // package version
            expression = "//package/metadata/version";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String packageVersion = node.getTextContent();
            logger.debug("Found package version from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setVersion(packageVersion);
            
            // package Title
            expression = "//package/metadata/title";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String packageTitle = node.getTextContent();
            logger.debug("Found package title from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setTitle(packageTitle);
            
            // authors
            expression = "//package/metadata/authors";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String packageAuthors = node.getTextContent();
            logger.debug("Found package authors from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setAuthors(packageAuthors);
            
            // owners
            expression = "//package/metadata/owners";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String packageOwners = node.getTextContent();
            logger.debug("Found package owners from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setOwners(packageOwners);
            
            // licenseUrl
            expression = "//package/metadata/licenseUrl";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String licenseUrl = node.getTextContent();
            logger.debug("Found package licenseUrl from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setLicenseUrl(new URL(licenseUrl));
            
            // projectUrl
            expression = "//package/metadata/projectUrl";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String projectUrl = node.getTextContent();
            logger.debug("Found package projectUrl from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setProjectUrl(new URL(projectUrl));
            
            // iconUrl
            expression = "//package/metadata/iconUrl";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String iconUrl = node.getTextContent();
            logger.debug("Found package iconUrl from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setIconUrl(new URL(iconUrl));
            
            // requireLicenseAcceptance
            expression = "//package/metadata/requireLicenseAcceptance";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String requireLicenseAcceptance = node.getTextContent();
            logger.debug("Found package requireLicenseAcceptance from nuspec : <" + node.getTextContent() + ">");
            if(requireLicenseAcceptance.equalsIgnoreCase("true")){
                this.nuspecPackageMetaData.setRequireLicenseAcceptance(true);
            }
            else{
                this.nuspecPackageMetaData.setRequireLicenseAcceptance(false);
            }
            //this.nuspecPackageMetaData.setIconUrl(new URL(iconUrl));
            
            // description
            expression = "//package/metadata/description";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String description = node.getTextContent();
            logger.debug("Found package description from nuspec : <" + node.getTextContent().substring(0, 10) + "...>");
            this.nuspecPackageMetaData.setDescription(description);
            
            // summary
            expression = "//package/metadata/summary";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String summary = node.getTextContent();
            logger.debug("Found package summary from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setSummary(summary);
            
            // releaseNotes
            expression = "//package/metadata/releaseNotes";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String releaseNotes = node.getTextContent();
            logger.debug("Found package releaseNotes from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setReleaseNotes(new URL(releaseNotes));
            
            // tags
            expression = "//package/metadata/tags";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String tags = node.getTextContent();
            logger.debug("Found package tags from nuspec : <" + node.getTextContent() + ">");
            StringTokenizer st = new StringTokenizer(tags);
            ArrayList<String> tagList = new ArrayList<String>();
            String aTag;
            while (st.hasMoreElements()) {
		aTag = 	st.nextElement().toString();
                    tagList.add(aTag);
                    logger.info("New tag added : <" + aTag + ">") ;
		}
            
            this.nuspecPackageMetaData.setTags(tagList);
            
            // projectSourceUrl
            expression = "//package/metadata/projectSourceUrl";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String projectSourceUrl = node.getTextContent();
            logger.debug("Found package projectSourceUrl from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setPackageSourceUrl(new URL(projectSourceUrl));
            
            // docsUrl
            expression = "//package/metadata/docsUrl";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String docsUrl = node.getTextContent();
            logger.debug("Found package docsUrl from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setDocsUrl(new URL(docsUrl));
            
            // bugTrackerUrl
            expression = "//package/metadata/docsUrl";
            node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            String bugTrackerUrl = node.getTextContent();
            logger.debug("Found package bugTrackerUrl from nuspec : <" + node.getTextContent() + ">");
            this.nuspecPackageMetaData.setBugTrackerUrl(new URL(bugTrackerUrl));
            
            
            
            //fetch dependencies
            ArrayList<NuspecDependency> dependencies = new ArrayList<NuspecDependency>();
            
            expression = "//package/metadata/dependencies/dependency";
            //NodeList dependenciesNodes = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            NodeList dependenciesNodes = doc.getElementsByTagName("dependency");
             //dependenciesNodes.getLength()
            for(int i = 0 ; i < dependenciesNodes.getLength() ; i++){
                Node lDepNode = dependenciesNodes.item(i);
                NuspecDependency lDep = new NuspecDependency();
                logger.debug("FOUND dependency : <" + ((Element)lDepNode).getAttribute("id") + ">");
                logger.debug("FOUND version version : <" + ((Element)lDepNode).getAttribute("version") + ">");
                logger.debug("Dependency <" + (i + 1 ) + "/" + dependenciesNodes.getLength() + ">");
                lDep.setId(((Element)lDepNode).getAttribute("id"));
                lDep.setVersion(((Element)lDepNode).getAttribute("version"));
                dependencies.add(lDep);
            }
            this.nuspecPackageMetaData.setDependencies(dependencies);
            
            
            
        }
        catch(FileSystemException ex){
            logger.error("Not able to fetch nuspec file because not able to mount VirtualFileSystem : " + ex.getMessage());
            throw ex;
        }
        catch(MalformedURLException ex){
            logger.error("Not able to fetch nuspec file because of bad nuspec file URL : " + ex.getMessage());
            throw ex;
        }
        catch(SAXException ex){
            logger.error("Was not able to deal with provided nuspec : " + ex.getMessage());
            throw ex;
        }
        catch(IOException ex){
            logger.error("Was not able to deal with the nuspec file : " + ex.getMessage());
            throw ex;
        }
        catch(ParserConfigurationException ex){
            logger.error("Was not able to properly initialize the XML Parser : " + ex.getMessage());
            throw ex;
        }
        catch(XPathExpressionException ex){
            logger.error("Was not able to run a xpath query : " + ex.getMessage());
            throw ex;
        }
        
    }
    
    public static void main(String[] args){
        String aPackage = "schemacrawler";
        String aVersion = "14.20.03";
        try{
            NuspecParser parser = new NuspecParser(aPackage, aVersion);
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
