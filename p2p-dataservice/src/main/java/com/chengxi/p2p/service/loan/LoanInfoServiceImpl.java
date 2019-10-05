package com.chengxi.p2p.service.loan;

import com.chengxi.p2p.constants.BizConstant;
import com.chengxi.p2p.mapper.loan.LoanInfoMapper;
import com.chengxi.p2p.model.loan.LoanInfo;
import com.chengxi.p2p.model.vo.PaginatinoVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Service
public class LoanInfoServiceImpl implements LoanInfoService {
    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Double queryHistoryAverageRate() {
        //先去redis缓存中获取该，有:直接使用，没有：去数据库查询并存放到redis缓存中

        //获取操作key=value的数据类型的redis的操作对象,并获取指定key的value值
        Double historyAverageRate = (Double) redisTemplate.opsForValue().get(BizConstant.HISTORY_AVERAGE_RATE);

        //判断是否有值
        if (null == historyAverageRate) {
            //没有值：去数据库查询
            historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
            //将该值存放到redis缓存中
            redisTemplate.opsForValue().set(BizConstant.HISTORY_AVERAGE_RATE, historyAverageRate, 15, TimeUnit.SECONDS);
        }

        return historyAverageRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanInfoByPage(paramMap);
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public PaginatinoVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap) {
        PaginatinoVO<LoanInfo> paginatinoVO = new PaginatinoVO<>();

        Long total = loanInfoMapper.selectTotal(paramMap);

        //查询总记录数
        paginatinoVO.setTotal(total);

        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByPage(paramMap);

        //查询显示数据
        paginatinoVO.setDataList(loanInfoList);


        return paginatinoVO;
    }

    @Override
    public PageInfo queryLoanInfoListByType(Map<String, Integer> paraMap) {
        PageHelper.startPage(paraMap.get("currentPage"), paraMap.get("pageSize"));
        List<LoanInfo> loanInfos = loanInfoMapper.selectLoanInfos(paraMap.get("productType"));
        PageInfo<LoanInfo> pageInfo = new PageInfo<>(loanInfos);

        return pageInfo;
    }
}
