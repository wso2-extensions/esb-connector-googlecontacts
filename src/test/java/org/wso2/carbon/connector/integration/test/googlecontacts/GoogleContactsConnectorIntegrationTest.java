/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.connector.integration.test.googlecontacts;

import org.apache.axiom.om.OMElement;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GoogleContactsConnectorIntegrationTest extends ConnectorIntegrationTestBase {
    private Map<String, String> esbRequestHeadersMap = new HashMap<String, String>();
    private Map<String, String> apiRequestHeadersMap = new HashMap<String, String>();

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        init("googlecontacts-connector-1.0.1");

        esbRequestHeadersMap.put("Accept-Charset", "UTF-8");
        esbRequestHeadersMap.put("Content-Type", "application/json");
        esbRequestHeadersMap.put("GData-Version", "3.0");
        esbRequestHeadersMap.put("If-None-Match", "Etag");

        String apiEndpointUrl = "https://www.googleapis.com/oauth2/v3/token?grant_type=refresh_token&client_id=" + connectorProperties.getProperty("clientId") +
                "&client_secret=" + connectorProperties.getProperty("clientSecret") + "&refresh_token=" + connectorProperties.getProperty("refreshToken");

        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndpointUrl, "POST", apiRequestHeadersMap);
        final String accessToken = apiRestResponse.getBody().getString("access_token");
        connectorProperties.put("accessToken", accessToken);
        apiRequestHeadersMap.put("Authorization", "Bearer " + accessToken);
        apiRequestHeadersMap.putAll(esbRequestHeadersMap);
    }

    /*
    *
    * Positive test case for retrieveAllContacts method with mandatory parameters.
    */
    @Test(description = "googlecontacts {retrieveAllContacts} integration test with mandatory parameters.")
    public void tesRretrieveAllContactsWithMandatoryParameters() throws IOException, XMLStreamException,
            XPathExpressionException, SAXException, ParserConfigurationException {
        esbRequestHeadersMap.put("Action", "urn:retrieveAllContacts");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveAllContacts_mandatory.txt");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/contacts/" + connectorProperties.getProperty("userEmail") + "/full";
        RestResponse<OMElement> apiRestResponse = sendXmlRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);

    }

    /**
     * Positive test case for retrieveSingleContact method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {retrieveSingleContact} integration test with mandatory parameters.")
    public void testRetrieveSingleContactWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:retrieveSingleContact");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/contacts/" + connectorProperties.getProperty("userEmail") + "/full/" + connectorProperties.getProperty("contactId");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveSingleContact_mandatory.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /* *
      * Negative test case for retrieveSingleContact method with mandatory parameters.
      */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {retrieveSingleContact} integration test with mandatory parameters.")
    public void testRetrieveSingleContactWithNegativeParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:retrieveSingleContact");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/contacts/" + connectorProperties.getProperty("userEmail") + "/full/" + connectorProperties.getProperty("invalid");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveSingleContact_negative.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Positive test case for retrieveContactsByQuery method with mandatory parameters.
     */
    @Test(description = "googlecontacts {retrieveContactsByQuery} integration test with mandatory parameters.")
    public void testRetrieveContactsByQueryWithMandatoryParameters() throws IOException, XMLStreamException,
            XPathExpressionException, SAXException, ParserConfigurationException, org.xml.sax.SAXException {
        esbRequestHeadersMap.put("Action", "urn:retrieveContactsByQuery");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveContactsByQuery_mandatory.txt");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/contacts/" + connectorProperties.getProperty("userEmail") + "/full?" + "max-results=" + connectorProperties.getProperty("maxResults");
        RestResponse<OMElement> apiRestResponse = sendXmlRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for retrieveAllContactGroups method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {retrieveAllContactGroups} integration test with mandatory parameters.")
    public void testRetrieveAllContactGroupsWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:retrieveAllContactGroups");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/groups/" + connectorProperties.getProperty("userEmail") + "/full";

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveAllContactGroups_mandatory.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for retrieveContactGroupsByQuery method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {retrieveContactsByQuery} integration test with mandatory parameters.")
    public void testRetrieveContactGroupsByQueryWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:retrieveContactGroupsByQuery");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/groups/" + connectorProperties.getProperty("userEmail") + "/full?updated-min=" + connectorProperties.getProperty("updatedMin");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveContactGroupsByQuery_mandatory.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /* *
      * Positive test case for retrieveSingleContactGroup method with mandatory parameters.
      */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {retrieveSingleContactGroup} integration test with mandatory parameters.")
    public void testRetrieveSingleContactGroupWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:retrieveSingleContactGroup");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/groups/" + connectorProperties.getProperty("userEmail") + "/full/" + connectorProperties.getProperty("groupID");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveSingleContactGroup_mandatory.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /* *
      * Positive test case for deleteContact method with mandatory parameters.
      */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {deleteContact} integration test with mandatory parameters.")
    public void testDeleteContactWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteContact");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/contacts/" + connectorProperties.getProperty("userEmail") + "/full/" + connectorProperties.getProperty("contactId");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteContact_mandatory.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
    }

    /* *
      * Negative test case for deleteContact method with mandatory parameters.
      */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {deleteContact} integration test with negative parameters.")
    public void testDeleteContactWithNegativeParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteContact");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/contacts/" + connectorProperties.getProperty("userEmail") + "/full/" + connectorProperties.getProperty("invalid");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteContact_negative.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Positive test case for retrieveContactPhoto method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {retrieveContactPhoto} integration test with mandatory parameters.")
    public void testRetrieveContactPhotoWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:retrieveContactPhoto");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/photos/media/" + connectorProperties.getProperty("userEmail") + "/" + connectorProperties.getProperty("contactId");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveContactPhoto_mandatory.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /* *
      * Negative test case for retrieveContactPhoto method with mandatory parameters.
      */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {retrieveContactPhoto} integration test with mandatory parameters.")
    public void testRetrieveContactPhotoWithNegativeParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:retrieveContactPhoto");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/photos/media/" + connectorProperties.getProperty("userEmail") + "/6c6c74e78a43a3dc";

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_retrieveContactPhoto_negative.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Positive test case for deleteContactGroup method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {deleteContactGroup} integration test with mandatory parameters.")
    public void testDeleteContactGroupWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteContactGroup");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/groups/" + connectorProperties.getProperty("userEmail") + "/full/" + connectorProperties.getProperty("groupID");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteContactGroup_mandatory.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "DELETE", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Positive test case for deleteContactPhoto method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {deleteContactPhoto} integration test with mandatory parameters.")
    public void testDeleteContactPhotoWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteContactPhoto");
        //String apiEndPoint = "https://www.googleapis.com/calendar/v3/calendars/" + parametersMap.get("calendarId") + "/acl/" + parametersMap.get("ruleId");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/photos/media/" + connectorProperties.getProperty("userEmail") + "/" + connectorProperties.getProperty("contactId");

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteContactPhoto_mandatory.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "DELETE", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Negative test case for deleteContactPhoto method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "googlecontacts {deleteContactPhoto} integration test with mandatory parameters.")
    public void testDeleteContactPhotoWithNegativeParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteContactPhoto");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/photos/media/" + connectorProperties.getProperty("userEmail") + "/6c6c74e78a43a3dcwewewewrwerwerewr";

        RestResponse<JSONObject> esbRestResponse = sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteContactPhoto_negative.txt");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "DELETE", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Positive test case for createContact method with mandatory parameters.
     */
    @Test(description = "googlecontacts {createContact} integration test with mandatory parameters.")
    public void testCreateContactWithMandatoryParameters() throws IOException, XMLStreamException,
            XPathExpressionException, SAXException, ParserConfigurationException {
        esbRequestHeadersMap.put("Action", "urn:createContact");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createContact_mandatory.txt");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
    }

    /**
     * Negative test case for createContact method with mandatory parameters.
     */
//    @Test(priority = 1, description = "googlecontacts {createContact} integration test with negative parameters.")
//    public void testCreateContactWithNegativeParameters() throws IOException, XMLStreamException,
//            XPathExpressionException, SAXException, ParserConfigurationException {
//        esbRequestHeadersMap.put("Action", "urn:createContact");
//        RestResponse<OMElement> esbRestResponse =
//                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createContact_negative.txt");
//        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
//    }

    /**
     * Positive test case for createContactGroup method with mandatory parameters.
     */
    @Test(priority = 1, description = "googlecontacts {createContactGroup} integration test with mandatory parameters.")
    public void testCreateContactGroupWithMandatoryParameters() throws IOException, XMLStreamException,
            XPathExpressionException, SAXException, ParserConfigurationException {
        esbRequestHeadersMap.put("Action", "urn:createContactGroup");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createContactGroup_mandatory.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
    }

    /**
     * Positive test case for createContactGroup method with negative parameters.
     */
    @Test(priority = 1, description = "googlecontacts {createContactGroup} integration test with negative parameters.")
    public void testCreateContactGroupWithNegativeParameters() throws IOException, XMLStreamException,
            XPathExpressionException, SAXException, ParserConfigurationException {
        esbRequestHeadersMap.put("Action", "urn:createContactGroup");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createContactGroup_negative.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
    }

    /**
     * Positive test case for batchCreateContacts method with mandatory parameters.
     */
    @Test(description = "googlecontacts {batchCreateContacts} integration test with mandatory parameters.")
    public void testBatchCreateContactsWithMandatoryParameters() throws IOException, XMLStreamException,
            XPathExpressionException, SAXException, ParserConfigurationException {
        esbRequestHeadersMap.put("Action", "urn:batchCreateContacts");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_batchCreateContacts_mandatory.txt");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for updateContact method with mandatory parameters.
     */
    @Test(priority = 1, description = "googlecontacts {updateContact} integration test with mandatory parameters.")
    public void testUpdateContactWithMandatoryParameters() throws IOException, XMLStreamException,
            XPathExpressionException, SAXException, ParserConfigurationException {
        esbRequestHeadersMap.put("Action", "urn:updateContact");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_updateContact_mandatory.txt");
        String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/feeds/contacts/" + connectorProperties.getProperty("userEmail") + "/full/" + connectorProperties.getProperty("contactIdUpdate");
        RestResponse<OMElement> apiRestResponse = sendXmlRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for batchCreateContactGroups method with mandatory parameters.
     */
    @Test(description = "googlecontacts {batchCreateContactGroups} integration test with mandatory parameters.")
    public void testBatchCreateContactGroupsWithMandatoryParameters() throws IOException, XMLStreamException,
            XPathExpressionException, SAXException, ParserConfigurationException {
        esbRequestHeadersMap.put("Action", "urn:batchCreateContactGroups");
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_batchCreateContactGroups_mandatory.txt");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }
}
