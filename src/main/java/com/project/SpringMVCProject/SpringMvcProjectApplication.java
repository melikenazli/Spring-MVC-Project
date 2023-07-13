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

@SpringBootApplication
public class SpringMvcProjectApplication implements CommandLineRunner{
	@Autowired
	private ProductRepository repository;

	@Autowired
	private ProductDocumentRepository documentRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repository.deleteAll();
		File directoryPath = new File("C:\\Users\\melik\\Desktop\\Spring-MVC-Project\\src\\main\\java\\com\\project\\SpringMVCProject\\XML_files");
		File[] filesList = directoryPath.listFiles();

		List<Product> products = readXMLFiles(filesList);
		repository.saveAll(products);
	}
	private List<Product> readXMLFiles(File[] filesList){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try{
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			List<Product> productList = new ArrayList<>();
			for (File file: filesList){
				String fileName = file.getName();
				String collectionName = fileName.replaceFirst("[.][^.]+$", "");
				if(!documentRepository.collectionExists(collectionName)){
					documentRepository.createCollection(collectionName);
				}

				Document doc = documentBuilder.parse(file);
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getElementsByTagName("product");
				for (int i=0;i<nodeList.getLength();i++){
					ProductDocument productDocument = getProductDocument(nodeList.item(i));
					documentRepository.save(productDocument, collectionName);

					List<Product> templist;
					templist = getProduct(nodeList.item(i));
					for (int j=0;j< templist.size();j++){
						productList.add(templist.get(j));
					}
				}
				for (Product product: productList){
					System.out.println(product);
				}
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
			System.out.println(dates);

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

	private ProductDocument getProductDocument(Node node){
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
		}
		return productDocument;
	}

	private String getTagValue(String tag, Element element){
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodeList.item(0);
		return node.getNodeValue();
	}
}


