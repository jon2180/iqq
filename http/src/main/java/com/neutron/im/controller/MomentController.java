package com.neutron.im.controller;

import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.entity.Moment;
import com.neutron.im.service.MomentService;
import com.neutron.im.util.StringUtil;
import com.neutron.im.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/moments")
public class MomentController {

    private final MomentService momentService;

    @Autowired
    public MomentController(MomentService momentService) {
        this.momentService = momentService;
    }

    /**
     * 新增动态内容
     */
    @PostMapping({"", "/"})
    public ResultVO postMoment(@RequestBody Map<String, Object> body, @RequestAttribute TokenUtil.JwtClaimsData claims) {
//        log.info("data: {}", body);
        final String title = ((String) body.getOrDefault("title", "")).trim();
        final String authorId = claims.getId();
        final Boolean isOriginal = (Boolean) body.getOrDefault("is_original", false);
        final String copyright = ((String) body.getOrDefault("license", "MIT")).trim();
        final String originalUrl = ((String) body.getOrDefault("original_url", "")).trim();

        final String contentType = ((String) body.getOrDefault("content_type", "activity")).trim();
        final String content = ((String) body.getOrDefault("content", "")).trim();
        final String[] tags = ((String) body.getOrDefault("tags", "")).trim().split("::");

        if (StringUtil.isEmpty(title) || StringUtil.isEmpty(authorId) || StringUtil.isEmpty(content)) {
            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "Empty Parameters");
        }

        final Moment moment = new Moment() {{
            setTitle(title);
            setAuthor_id(authorId);
            setContent_type(contentType.equals("activity") ? 0 : 1);
            setIs_original(isOriginal ? 0 : 1);
            setContent(content);
            setCopyright(copyright);
            setCreate_time(new Timestamp(System.currentTimeMillis()));
            setUpdate_time(new Timestamp(System.currentTimeMillis()));
            setVersion("v1.0");
            setStatus(0);
        }};
        final int insertResult = momentService.insertMoment(moment);

        if (insertResult != 1) {
            return ResultVO.failed(StatusCode.S500_INTERNAL_SERVER_ERROR, "Insert Failed", null);
        }
//        Map<String, Object> data = new HashMap<>() {{
//            put("moment", moment);
//            put("url", moment.getId());
//        }};

        return ResultVO.success(moment);
    }

    /**
     * GET /moments
     *
     * @param id        用户 id
     * @param page_no   第几页 从 0 开始
     * @param page_size 每页多少条数据
     * @return 列表
     */
    @GetMapping(value = {"", "/"})
    public ResultVO getMoments(
        @RequestParam(value = "uid", required = false) String id,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(required = false) Integer page_no,
        @RequestParam(required = false) Integer page_size,
        @RequestParam String type) {
        if (StringUtil.isEmpty(type)) {
            type = "activity";
        }
        List<Moment> moments = momentService.findByContentType("codesnips".equals(type) ? 1 : 0);
        return ResultVO.success(moments);
    }

    /**
     * 获取具体的某一个 动态
     *
     * @param momentId
     * @return
     */
    @GetMapping("/{momentId}")
    public Object getMoment(@PathVariable String momentId) {
        Moment moment = momentService.findById(momentId);
        return ResultVO.success(moment);
    }

    @PutMapping("/{momentId}")
    public String putMoment(@PathVariable("momentId") String id) {
        return "";
    }

    @DeleteMapping("/{momentId}")
    public String deleteMoment(@PathVariable("momentId") String id) {
        return "";
    }

    @GetMapping({"/{momentId}/comments"})
    public ResultVO getCommentsOfMoment(
        @PathVariable String momentId,
        @RequestParam(required = false) Integer page_no,
        @RequestParam(required = false) Integer page_size
    ) {
        return ResultVO.failed(StatusCode.S404_NOT_FOUND, "404 Not Found");
    }
}
