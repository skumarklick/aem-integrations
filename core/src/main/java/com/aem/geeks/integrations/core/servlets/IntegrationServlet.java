package com.aem.geeks.integrations.core.servlets;

import com.aem.geeks.integrations.core.utils.ResolverUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.util.Iterator;


@Component(service = Servlet.class)
@SlingServletPaths(
        value = {"/bin/aemintegration"}
)
public class IntegrationServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(IntegrationServlet.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
        //String jsonP="/content/aemgeeks/us/en/author/jcr:content/parsys-8/author.geeks.json";
        //invalidateCache(jsonP,dispatcherFlusher);
        LOG.info("\n ---------------SERVLET INTEGRATION-----------------");
        //final ResourceResolver resourceResolver = req.getResourceResolver();

        JSONArray pagesArray = new JSONArray();
        try {
            final ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            Page page = resourceResolver.adaptTo(PageManager.class).getPage("/content/aemgeeks/us/en");
            Iterator<Page> childPages = page.listChildren(null,true);
            while (childPages.hasNext()) {
                Page childPage = childPages.next();
                JSONObject pageObject = new JSONObject();
                pageObject.put(childPage.getTitle(), childPage.getPath().toString());
                pagesArray.put(pageObject);
            }
            resp.setContentType("application/json");
            resp.getWriter().write(pagesArray.toString());
        } catch (Exception e) {
            LOG.info("\n ERROR GET - {} ", e.getMessage());
        }
    }
}
