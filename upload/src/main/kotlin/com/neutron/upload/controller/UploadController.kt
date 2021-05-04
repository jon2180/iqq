package com.neutron.upload.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*

@RestController
@RequestMapping("/")
class UploadController {

    @PostMapping("/upload")
    fun upload(@RequestParam("file") files: Array<MultipartFile>?) {
//        if (files == null) {
//            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "No Files to Upload", null)
//        }
//        val filePath = "D:/Downloads/"
//        val tempDir = File(filePath)
//        if (!tempDir.exists()) return ResultVO.failed(StatusCode.S500_FILE_STORAGE_ERROR, "InvalidTempDirectory", null)
//
//        val messages = Array<String>(files.size) {
//            var i = 0
//            while (i < files.size) {
//                val file = files[i]
//                if (file.isEmpty) {
//                    messages.get(i) = "上传第" + (i + 1) + "文件失败, 文件为空"
//                    i++
//                    continue
//                }
//                val fileName = file.originalFilename
//                val dest = File(filePath + fileName)
//                try {
//                    file.transferTo(dest)
//                    messages.get(i) = "第" + (i + 1) + "个文件上传成功"
//                } catch (e: IOException) {
//                    HomeController.log.error(e.toString(), e)
//                    messages.get(i) = "上传第" + i++ + "个文件失败，转换失败"
//                }
//                i++
//            }
//        }
//        HomeController.log.info(Arrays.toString(messages))
//        return ResultVO.success(messages)
    }
}
