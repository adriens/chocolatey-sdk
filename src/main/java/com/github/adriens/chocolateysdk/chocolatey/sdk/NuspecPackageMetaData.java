/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.chocolateysdk.chocolatey.sdk;

import java.net.URL;
import java.util.List;

/**
 *
 * @author salad74
 */
public class NuspecPackageMetaData {

    /**
     * @return the dependencies
     */
    public List<NuspecDependency> getDependencies() {
        return dependencies;
    }

    /**
     * @param dependencies the dependencies to set
     */
    public void setDependencies(List<NuspecDependency> dependencies) {
        this.dependencies = dependencies;
    }

    private String id;
    private String version;
    private String title;
    private String authors;
    private String owners;
    private URL licenseUrl;
    private URL projectUrl;
    private URL iconUrl;
    private boolean requireLicenseAcceptance;
    private String description;
    private String summary;
    private URL releaseNotes;
    private List<String> tags;
    private URL projectSourceUrl;
    private URL packageSourceUrl;
    private URL docsUrl;
    private URL bugTrackerUrl;
    
    private List<NuspecDependency> dependencies;
        
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the authors
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * @param authors the authors to set
     */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /**
     * @return the owners
     */
    public String getOwners() {
        return owners;
    }

    /**
     * @param owners the owners to set
     */
    public void setOwners(String owners) {
        this.owners = owners;
    }

    /**
     * @return the licenseUrl
     */
    public URL getLicenseUrl() {
        return licenseUrl;
    }

    /**
     * @param licenseUrl the licenseUrl to set
     */
    public void setLicenseUrl(URL licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    /**
     * @return the projectUrl
     */
    public URL getProjectUrl() {
        return projectUrl;
    }

    /**
     * @param projectUrl the projectUrl to set
     */
    public void setProjectUrl(URL projectUrl) {
        this.projectUrl = projectUrl;
    }

    /**
     * @return the iconUrl
     */
    public URL getIconUrl() {
        return iconUrl;
    }

    /**
     * @param iconUrl the iconUrl to set
     */
    public void setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * @return the requireLicenseAcceptance
     */
    public boolean isRequireLicenseAcceptance() {
        return requireLicenseAcceptance;
    }

    /**
     * @param requireLicenseAcceptance the requireLicenseAcceptance to set
     */
    public void setRequireLicenseAcceptance(boolean requireLicenseAcceptance) {
        this.requireLicenseAcceptance = requireLicenseAcceptance;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the releaseNotes
     */
    public URL getReleaseNotes() {
        return releaseNotes;
    }

    /**
     * @param releaseNotes the releaseNotes to set
     */
    public void setReleaseNotes(URL releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    /**
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return the projectSourceUrl
     */
    public URL getProjectSourceUrl() {
        return projectSourceUrl;
    }

    /**
     * @param projectSourceUrl the projectSourceUrl to set
     */
    public void setProjectSourceUrl(URL projectSourceUrl) {
        this.projectSourceUrl = projectSourceUrl;
    }

    /**
     * @return the packageSourceUrl
     */
    public URL getPackageSourceUrl() {
        return packageSourceUrl;
    }

    /**
     * @param packageSourceUrl the packageSourceUrl to set
     */
    public void setPackageSourceUrl(URL packageSourceUrl) {
        this.packageSourceUrl = packageSourceUrl;
    }

    /**
     * @return the docsUrl
     */
    public URL getDocsUrl() {
        return docsUrl;
    }

    /**
     * @param docsUrl the docsUrl to set
     */
    public void setDocsUrl(URL docsUrl) {
        this.docsUrl = docsUrl;
    }

    /**
     * @return the bugTrackerUrl
     */
    public URL getBugTrackerUrl() {
        return bugTrackerUrl;
    }

    /**
     * @param bugTrackerUrl the bugTrackerUrl to set
     */
    public void setBugTrackerUrl(URL bugTrackerUrl) {
        this.bugTrackerUrl = bugTrackerUrl;
    }
    
}
