/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.chocolateysdk.chocolatey.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author salad74
 */
public class DownloadTest {

    public static void main(String[] args) {

        String fromFile = "https://chocolatey.org/api/v2/package/Firefox/59.0.1";
        String toFile = "C:\\Users\\salad74\\tmp\\package.nupkg";

        try {

            //connectionTimeout, readTimeout = 10 seconds
            //FileUtils.copyURLToFile(new URL(fromFile), new File(toFile), 10000, 10000);
            FileSystemManager fsManager = VFS.getManager();
            String packageName = "Firefox";
            String packageVersion = "59.0.1";
            URL nuPkgUrl = PackageWrapper.composeNuPkgDownloadURL(packageName, packageVersion);
            //FileObject jarFile = fsManager.resolveFile("zip:https://packages.chocolatey.org/schemacrawler.14.20.03.nupkg");
            FileObject nuPkgFile = fsManager.resolveFile("zip:" + nuPkgUrl.toString());
            //FileObject jarFile = fsManager.resolveFile("zip:https://chocolatey.org/api/v2/package/Firefox/59.0.1");
            

            // List the children of the Jar file
            FileObject[] children = nuPkgFile.getChildren();
            System.out.println("Children of " + nuPkgFile.getName().getURI());
            FileObject nuspecFile = null;
            for (int i = 0; i < children.length; i++) {
                System.out.println(children[i].getName().getBaseName());
                //get the main (one and only one) package nupec
                if (children[i].getName().getBaseName().endsWith(".nuspec")) {
                    nuspecFile = children[i];
                    System.out.println("Found nuspec file : <" + nuspecFile.getName() + ">");
                    break;
                }
            }
            // build the dom

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(nuspecFile.getContent().getInputStream());
            System.out.println("Successfuly loaded xml");
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            ///////////////////////////////////////////////////////
            // get nodes
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            // get id
            String expression = "//package/metadata/id";
            Node node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            System.out.println("Found node id : <" + node.getTextContent() + ">");
            
            // fetch dependencies
            expression = "//package/metadata/dependencies/dependency";
            //NodeList dependenciesNodes = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
            NodeList dependenciesNodes = doc.getElementsByTagName("dependency");
             //dependenciesNodes.getLength()
            for(int i = 0 ; i < dependenciesNodes.getLength() ; i++){
                Node lDepNode = dependenciesNodes.item(i);
                System.out.println("FOUND dependency : <" + ((Element)lDepNode).getAttribute("id") + ">");
                System.out.println("FOUND version: <" + ((Element)lDepNode).getAttribute("version") + ">");
                System.out.println("Dependency <" + i + ">");
            }
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(XPathExpressionException ex){
            ex.printStackTrace();
        }

    }
}
