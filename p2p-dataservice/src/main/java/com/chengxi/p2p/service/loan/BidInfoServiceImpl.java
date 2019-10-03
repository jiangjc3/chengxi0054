package com.chengxi.p2p.service.loan;

import com.chengxi.p2p.constants.BizConstant;
import com.chengxi.p2p.mapper.loan.BidInfoMapper;
import com.chengxi.p2p.mapper.loan.LoanInfoMapper;
import com.chengxi.p2p.mapper.user.FinanceAccountMapper;
import com.chengxi.p2p.model.loan.BidInfo;
import com.chengxi.p2p.model.vo.BidUserTop;
import com.chengxi.p2p.service.loan.BidInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Service
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Double queryAllBidMoney() {
        //获取指定key的操作对象
        BoundValueOperations<Object, Object> boundValueOps = redisTemplate.boundValueOps(BizConstant.ALL_BID_MONEY);

        //获取指定key的value值
        Double allBidMoney = (Double) boundValueOps.get();

        //判断是否有值
        if (null == allBidMoney) {
            //去数据库查询
            allBidMoney = bidInfoMapper.selectAllBidMoney();

            //存放到redis缓存中
            boundValueOps.set(allBidMoney,15, TimeUnit.MINUTES);

        }

        return allBidMoney;
    }

    @Override
    public List<BidInfo> queryBidInfoListByLoanId(Integer id) {
        return bidInfoMapper.selectBidInfoListByLoanId(id);
    }

    @Override
    public List<BidUserTop> queryBidUserTop() {
        List<BidUserTop> bidUserTopList = new ArrayList<BidUserTop>();

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(BizConstant.INVEST_TOP, 0, 9);

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();

        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            String phone = (String) next.getValue();
            Double score = next.getScore();
            BidUserTop bidUserTop = new BidUserTop();
            bidUserTop.setPhone(phone);
            bidUserTop.setScore(score);

            bidUserTopList.add(bidUserTop);
        }
        return bidUserTopList;
    }
}
