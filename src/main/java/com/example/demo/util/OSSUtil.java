package com.example.demo.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.example.demo.config.OSSConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URL;

public class OSSUtil implements InitializingBean {
	
	@Autowired
    private OSSConfig oSSConfig;
	
    private String bucketName;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    
    private static final Logger log = Logger.getLogger(OSSUtil.class);

    /**
     * 上传到OSS服务器 如果同名文件会覆盖服务器上的
     *
     * @param folderName 文件夹名称
     * @param fileName 文件名称 包括后缀名
     * @param instream 文件流
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String upload(String folderName,String fileName,InputStream instream) {
    	OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String resultStr = "";
        try {

            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folderName +"/"+ fileName, instream,objectMetadata);
            // 解析结果
            resultStr = putResult.getETag();
        }catch(Exception e) {
        	log.error("OSSUtil.upload"+e.getMessage());
        }finally {
            try {
                if (instream != null) {
                    instream.close();
                }
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            } catch (IOException e1) {
                log.error("OSSUtil.upload:finally"+e1.getMessage());
            }
        }
        return resultStr;
    }
    
    /**
     * 以文件流形式去下载
     * @param folderName oss文件夹名称
     * @param fileName 文件名称 包括后缀名
     * @return 流数据
     */
    public InputStreamReader download(String folderName,String fileName) {
    	OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    	InputStreamReader reader=null;
        try {

            OSSObject ossObject = ossClient.getObject(new GetObjectRequest(bucketName, folderName+"/" +fileName));
            reader=new InputStreamReader(ossObject.getObjectContent());
            
        }catch (Exception e) {
            log.error("OSSUtil.download"+e.getMessage());
        }
        finally {
            try {
            	if (ossClient != null) {
                    ossClient.shutdown();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return reader;
    }
    
    /**
     * 以本地文件形式去下载
     * @param folderName 文件夹名称
     * @param fileName 文件名称 包括后缀名
     * @param localFileUrl 本地路径
     */
    public void downloadAsFile(String folderName,String fileName,String localFileUrl) {
    	OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.getObject(new GetObjectRequest(bucketName, folderName+"/" +fileName),new File(localFileUrl));           
        }catch(Exception e) {
        	log.error("OSSUtil.downloadAsFile"+e.getMessage());
        }
        finally {
            try {
            	if (ossClient != null) {
                    ossClient.shutdown();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 上传图片
     *
     * @param folderName oss文件夹名称
     * @param fileName 存储的文件名称 包括后缀名
     * @param url 要上传的文件路径
     */
    public void uploadUrl(String folderName,String fileName, String url) {
        InputStream instream = null;
        try {
            instream = new URL(url).openStream();
            upload(folderName,fileName, instream);
        } catch (IOException e) {
            log.error("OSSUtil.uploadUrl"+e.getMessage());
        } finally {
            if (null != instream) {
                try {
                    instream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 下载文件
     */
    public void exportOssFile(OutputStream os, String folderName,String fileName) {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            OSSObject ossObject = ossClient.getObject(new GetObjectRequest(bucketName, folderName+"/" +fileName));
            // 读取文件内容。
            in = new BufferedInputStream(ossObject.getObjectContent());
            out = new BufferedOutputStream(os);
            byte[] buffer = new byte[1024];
            int lenght = 0;
            while ((lenght = in.read(buffer)) != -1) {
                out.write(buffer, 0, lenght);
            }
        }catch (Exception e){
            log.error("OSSUtil.exportOssFile"+e.getMessage());
        }finally {
            try {
                if (out != null){
                    out.flush();
                    out.close();
                }
            }catch (IOException e){
                log.error(e.getMessage());
            }
            try {
                if (in != null) {
                    in.close();
                }
            }catch (IOException e){
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.bucketName = oSSConfig.getBucketName();
        this.endpoint = oSSConfig.getEndpoint();
        this.accessKeyId = oSSConfig.getAccessKeyId();
        this.accessKeySecret = oSSConfig.getAccessKeySecret();
    }
}
