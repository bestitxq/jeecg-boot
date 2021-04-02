package org.jeecg.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.jeecg.entity.Product;
import org.jeecg.mapper.xml.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/testes")
class ElasticsearchTests {

    @Autowired
    ProductDao productDao;

    @Autowired
    private ElasticsearchRestTemplate client;

    // 查询
    @RequestMapping("testSearch")
    public void testSearch() throws IOException {
        // 1.创建查询请求对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 2.构建搜索条件 使用QueryBuilders工具类创建
        // 精确查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "liuyou");
        // 匹配查询
        // MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        // 3.添加条件到请求
        nativeSearchQueryBuilder.withQuery(termQueryBuilder);
        // 设置高亮
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder());
        // 4.客户端查询请求
        SearchHits<Product> hits = client.search(nativeSearchQueryBuilder.build(), Product.class);
        System.out.println(JSON.toJSONString(hits));
        System.out.println("=======================");
        hits.forEach(System.out::println);
    }

    @RequestMapping("addProduct")
    public Product addProduct(@RequestBody Product product) {
        //新增单个
        productDao.save(product);
        System.out.println(product);
        return product;
    }

    @RequestMapping("addProductList")
    public List<Product> addProductList(@RequestBody List<Product> product) {
        //批量新增
        productDao.saveAll(product);
        System.out.println(product);
        return product;
    }

    @RequestMapping("/deleteByName")
    public String delete(String name) {
        //按name删除
        productDao.deleteByName(name);
        return "success";
    }

    @RequestMapping("/getByName")
    public List<Product> getByName(String name) {
        //按name查询
        List<Product> list = productDao.findByName(name);
        return list;
    }

    @RequestMapping("/getByNameAndCategory")
    public List<Product> getByNameAndCategory(String name, String category) {
        //按name和category查询
        List<Product> list = productDao.findByNameAndCategory(name, category);
        return list;
    }

    @RequestMapping("/get_by_page")
    public List<Product> getByNameAndCategory(String name, String category, Integer start, Integer size) {
        //按name和category分页查询
        Pageable pageable = PageRequest.of(start, size);
        Page<Product> page = productDao.findByNameAndCategory(name, category, pageable);
        for (Product product : page.getContent()) {
            System.out.println(product);
        }
        return page.getContent();
    }

    @RequestMapping("/like_by_page")
    public List<Product> likeByNameAndCategory(String name, String category, Integer start, Integer size) {
        //分页模糊查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //wildcardQuery 统配符模糊查询模糊查询  *表示任意多个字符 ?表示任意一个字符
        QueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery("name", "*" + name + "*");
        //queryStringQuery 默认左右模糊查询
        QueryBuilder queryBuilder2 = QueryBuilders.queryStringQuery(category).field("category");
        //should 类似于or
        QueryBuilder queryBuilder3 = QueryBuilders.boolQuery().should(queryBuilder1).should(queryBuilder2);
        //must 必须符合的条件      mustNot 必须不符合的条件
        //QueryBuilder queryBuilder3 = QueryBuilders.boolQuery().must(queryBuilder1).must(queryBuilder2);
        Pageable pageable = PageRequest.of(start, size);
        nativeSearchQueryBuilder.withQuery(queryBuilder3);
        nativeSearchQueryBuilder.withPageable(pageable);
        SearchHits<Product> searchHits = client.search(nativeSearchQueryBuilder.build(), Product.class);
        // 不确定会不会出现空指针情况，之后再来测试控制，现在先加上空值过滤避免空指针异常
        List<Product> products1 = searchHits.stream()
                .filter(x -> ObjectUtil.isNotEmpty(x.getContent()))
                .map(SearchHit::getContent).collect(Collectors.toList());
        List<Product> products2 = Convert.toList(Product.class, searchHits.stream().toArray());
        return products1;
    }

}