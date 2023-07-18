package com.project.SpringMVCProject;

import com.project.SpringMVCProject.models.Product;
import com.project.SpringMVCProject.models.ProductDocument;
import com.project.SpringMVCProject.repository.ProductDocumentRepository;
import com.project.SpringMVCProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The beginning point of the application.
 * 
 * This class handles reading XML files which contain product information and stores them inside relational and nosql databases.
 * 
 * Each XML file stores the products for different websites.
 * 
 * @author Melike Nazlı Karaca
 */

@SpringBootApplication
public class SpringMvcProjectApplication implements CommandLineRunner{
	@Autowired
	private ProductRepository repository;

	@Autowired
	private ProductDocumentRepository documentRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcProjectApplication.class, args);
	}

	/**
	 * Finds each XML file to read inside the XML_files folder.
	 * Saves the read products inside MySQL database.
	 */
	@Override
	public void run(String... args) throws Exception {
		repository.deleteAll();
		File directoryPath = new File("src\\main\\java\\com\\project\\SpringMVCProject\\XML_files");
		File[] filesList = directoryPath.listFiles();

		List<Product> products = readXMLFiles(filesList);
		repository.saveAll(products);
	}

	/**
	 * This method reads the XML files one by one to store the product information inside both MySQL database and MongoDB.
	 * Names of the files are used to assign the web site name for each product where it is sold.
	 * In this method, products are added to MongoDB one by one as they are being read.
	 * 
	 * @param filesList list of the files inside the directory
	 * @return the list of all products
	 */
	private List<Product> readXMLFiles(File[] filesList){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try{
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			List<Product> productList = new ArrayList<>();
			for (File file: filesList){
				String fileName = file.getName();
				String websiteName = fileName.replaceFirst("[.][^.]+$", "");

				Document doc = documentBuilder.parse(file);
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getElementsByTagName("product");
				for (int i=0;i<nodeList.getLength();i++){
					ProductDocument productDocument = getProductDocument(nodeList.item(i), websiteName);
					documentRepository.save(productDocument);

					List<Product> templist;
					templist = getProduct(nodeList.item(i));
					for (int j=0;j< templist.size();j++){
						productList.add(templist.get(j));
					}
				}
				/*for (Product product: productList){
					System.out.println(product);
				}*/
			}
			return productList;
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method reads the tags and creates Product object
	 * Each product is represented as a list where each item is the same product but with different price and date
	 * Products are read to be stored in MySQL
	 * @param node Node object with product tag
	 * @return product within the given node
	 */
	private List<Product> getProduct(Node node){
		List<Product> productWithDifferentPrices = new ArrayList<>();
		if (node.getNodeType() == Node.ELEMENT_NODE){
			Element element = (Element) node;

			Node pricesNode = element.getElementsByTagName("prices").item(0);
			Element pricesElement = (Element) pricesNode;
			NodeList nodeList = pricesElement.getElementsByTagName("price");
			for (int i=0;i< nodeList.getLength();i++){
				Product product = new Product();
				product.setPrice(Long.parseLong(nodeList.item(i).getTextContent()));
				productWithDifferentPrices.add(product);
			}

			Node datesNode = element.getElementsByTagName("dates").item(0);
			Element datesElement = (Element) datesNode;
			NodeList nodeList2 = datesElement.getElementsByTagName("date");
			List<LocalDate> dates = new ArrayList<>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			for (int i=0;i< nodeList2.getLength();i++){
				dates.add(LocalDate.parse(nodeList2.item(i).getTextContent(), formatter));
			}
			//System.out.println(dates);

			for (int i=0;i< productWithDifferentPrices.size();i++){
				Product tempProduct = productWithDifferentPrices.get(i);
				tempProduct.setProductId(Long.parseLong(getTagValue("productId", element)));
				tempProduct.setTitle(getTagValue("title", element));
				tempProduct.setBrand(getTagValue("brand", element));
				tempProduct.setCategory(getTagValue("category", element));
				tempProduct.setUrl(getTagValue("url", element));
				tempProduct.setDate(dates.get(i));
			}
		}
		return productWithDifferentPrices;
	}

	/**
	 * This method reads each product to be stored in MongoDB
	 * @param node Node object with product tag
	 * @param websiteName to assign website field of the PrductDocument which is hold in MongoDB
	 * @return product within the node as ProductDocument object
	 */
	private ProductDocument getProductDocument(Node node, String websiteName){
		ProductDocument productDocument = new ProductDocument();
		if (node.getNodeType() == Node.ELEMENT_NODE){
			Element element = (Element) node;

			productDocument.setTitle(getTagValue("title", element));

			Node pricesNode = element.getElementsByTagName("prices").item(0);
			Element pricesElement = (Element) pricesNode;
			NodeList nodeList = pricesElement.getElementsByTagName("price");
			List<Long> prices = new ArrayList<>();
			for (int i=0;i<nodeList.getLength();i++){
				prices.add(Long.parseLong(nodeList.item(i).getTextContent()));
			}
			productDocument.setPrices(prices);

			Node datesNode = element.getElementsByTagName("dates").item(0);
			Element datesElement = (Element) datesNode;
			NodeList nodeList2 = datesElement.getElementsByTagName("date");
			List<LocalDate> dates = new ArrayList<>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			for (int i=0;i< nodeList2.getLength();i++){
				dates.add(LocalDate.parse(nodeList2.item(i).getTextContent(), formatter));
			}
			productDocument.setDates(dates);
			productDocument.setWebsite(websiteName);
		}
		return productDocument;
	}

	/**
	 * Method to get tag value
	 * @param tag name of the xml tag
	 * @param element Element object to get the value of
	 * @return value of the tag represented as String
	 */
	private String getTagValue(String tag, Element element){
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodeList.item(0);
		return node.getNodeValue();
	}
}


