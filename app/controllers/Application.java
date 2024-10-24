package controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import models.ApexList;
import models.ApexType;
import models.TypeFactory;

import org.codehaus.jackson.map.ObjectMapper;

import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Scope;
import play.mvc.results.RenderTemplate;
import play.mvc.results.Result;
import play.templates.Template;
import play.templates.TemplateLoader;

public class Application extends Controller {

	public static void index(String json, String className) {
		if (className == null) className = "JSON2Apex";
   render(Template("index.html").params(json, className).render());
	}
	
 	public static void makeApex(@Required String json, @Required String className, @Required boolean createParseCode) {
	 	if (Validation.hasErrors()) {
	        flash.error("Oops, please enter your json and className!");
	        index(json, className);
	    }
	 	json = json.trim();
	 	className = className.trim();
		try {
			ObjectMapper m = new ObjectMapper();
			Object o = m.readValue(json, Object.class);
			TypeFactory factory = new TypeFactory();
	 		ApexType root = factory.typeOfObject(className, o);
	 		boolean needsParse = factory.shouldGenerateExplictParse();
	 		
			Map<String, Object> templateBinding = new HashMap<>();
	 		templateBinding.put("className", className);
	 		templateBinding.put("json", json);
	 		templateBinding.put("root", root);
	 		templateBinding.put("classes", factory.getClasses());
			templateBinding.put("types", factory.allTypes());
	 		templateBinding.put("needsExplictParse", needsParse);
	
			if (createParseCode || needsParse) {
				renderApexZip("makeApexWithParse.txt", "makeApexWithParseTest.txt", className, templateBinding);
			} else {
				renderApexZip("makeApex.txt", "makeApexTest.txt", className, templateBinding);
			}			
		} catch (IOException ex) {
			flash.error("sorry, unable to parse your json: " + ex.getMessage());
		}
		index(json, className);
    }
 	
 	static void renderApexZip(String primaryTemplate, String testCodeTemplate, String className, Map<String, Object> templateBinding) {
 		String test = applyTemplate("Application/" + testCodeTemplate, templateBinding);
 	 	String primary = applyTemplate("Application/" + primaryTemplate, templateBinding);
 		ByteArrayOutputStream os = new ByteArrayOutputStream();
 		ZipOutputStream zos = new ZipOutputStream(os);
 		ZipEntry pe = new ZipEntry(className + ".apxc");
 		try{
	 		zos.putNextEntry(pe);
	 		zos.write(primary.getBytes(StandardCharsets.UTF_8));
	 		zos.closeEntry();
	 		ZipEntry te = new ZipEntry(className + "_Test.apxc");
	 		zos.putNextEntry(te);
	 		zos.write(test.getBytes(StandardCharsets.UTF_8));
	 		zos.closeEntry();
	 		zos.close();
	 		
	 		renderBinary(new ByteArrayInputStream(os.toByteArray()), className + ".zip");
 		} catch (IOException ex) {
 			flash.error("sorry, unable to generate your apex code: " + ex.getMessage());
 		}
 	}
 	
 	static String applyTemplate(String templateName, Map<String, Object> args) {
        Template template = TemplateLoader.load(templateName);
        return template.render(args);
 	}
}