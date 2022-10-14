package com.snowyyy.picc.controller;


import com.snowyyy.picc.common.GlobalResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/upload")
@Api(tags = "文件接口")
public class UpLoadController {
    // 设置固定的日期格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // 日志打印
    private Logger log = LoggerFactory.getLogger("FileController");

    // 文件上传 （可以多文件上传）
    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public GlobalResult fileUpload(@RequestParam(value = "dir") String dir,
                                    HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        String filePath="/home/picc/"+dir;
        // 得到文件保存的位置以及新文件名
        File dest = new File(filePath + fileName);
        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            // 打印日志
            log.info("上传成功，当前上传的文件保存在 {}",filePath + fileName);
            // 自定义返回的统一的 JSON 格式的数据，可以直接返回这个字符串也是可以的。
            GlobalResult result = GlobalResult.build(200, "上传成功", null);
            return result;
        } catch (IOException e) {
            log.error(e.toString());
        }
        // 待完成 —— 文件类型校验工作
        GlobalResult result = GlobalResult.build(400, "上传失败", null);
        return result;
    }
    // 文件下载
    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    public void fileDownload(@RequestParam(value = "dir") String dir,HttpServletResponse response){
        File fileDir = new File("E://music_eg");
        File[] files=fileDir.listFiles();
        for(File file:files){// 穿件输入对象
            try {
                FileInputStream fis = new FileInputStream(file);
                // 设置相关格式
                response.setContentType("application/force-download");
                // 设置下载后的文件名以及header
                response.addHeader("Content-disposition", "attachment;fileName=" + "a.txt");
                // 创建输出对象
                OutputStream os = response.getOutputStream();
                // 常规操作
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = fis.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                fis.close();
            }catch (IOException e) {
                log.error(e.toString());
            }

        }
    }
}
