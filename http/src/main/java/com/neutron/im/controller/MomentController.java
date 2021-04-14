package com.neutron.im.controller;

import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moments")
public class MomentController {

    /**
     * 新增动态内容
     */
    @PostMapping("/")
    public ResultVO postMoment() {
        return ResultVO.failed(StatusCode.S404_NOT_FOUND, "No Implementation", null);
    }

    /**
     * GET /moments
     *
     * @param id    用户 id
     * @param page  第几页 从 0 开始
     * @param count 每页多少条数据
     * @return 列表
     */
    @GetMapping("/")
    public List<Object> getMoments(@RequestParam("uid") String id, @RequestParam int page, @RequestParam int count) {
        return null;
    }

    /**
     * 获取具体的某一个 动态
     *
     * @param momentId
     * @return
     */
    @GetMapping("/{momentId}")
    public Object getMoment(@PathVariable String momentId) {
        return ResultVO.success(null);
    }

    @PutMapping("/{momentId}")
    public String putMoment(@PathVariable("momentId") String id) {
        return "";
    }

    @DeleteMapping("/{momentId}")
    public String deleteMoment(@PathVariable("momentId") String id) {
        return "";
    }
}
