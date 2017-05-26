package com.ydp.pwgl.service.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class RedisSessionDao extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

    @Autowired
    @Qualifier("stringSerializer")
    protected RedisSerializer<String> stringSerializer;


    @Autowired
    @Qualifier("jedisPool")
    private JedisPool jedisPool;

    /**
     * shiro-redis的session对象前缀
     */
    private String keyPrefix = "pwgl:shiro_session:";

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException{
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }

        byte[] key = convertByteKey(session.getId());
        byte[] value = convertByteValue(SerilizableUtils.serilize(session));
        int expire = 60 * 30;	//设置过期时间为30分钟
        setByteValue(0, key, value, expire);
    }

    @Override
    public void delete(Session session) {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }

        removeByteKey(0, this.convertByteKey(session.getId()));
    }

    //用来统计当前活动的session
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();
        byte[] k = stringSerializer.serialize(this.keyPrefix + "*");
        Set<byte[]> keys = keys(0, k);
        if(keys != null && keys.size()>0){
            for(byte[] key:keys){
                String session=getValue(0, key);
                if(session==null){
                    logger.error("session id null");
                    removeByteKey(0, this.convertByteKey(key));
                    continue;
                }
                Session s = SerilizableUtils.deserialize(session);
                sessions.add(s);
            }
        }
        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId == null){
            logger.error("session id is null");
            return null;
        }

        String session=getValue(0, convertByteKey(sessionId));
        if(session==null){
            logger.error("session is null");
            removeByteKey(0, this.convertByteKey(sessionId));
            return null;
        }
        Session s = SerilizableUtils.deserialize(session);
        return s;
    }


    /**
     * 获得byte[]型的key
     * @param sessionId
     * @return
     */
    private byte[] convertByteKey(Serializable sessionId){
        String preKey = this.keyPrefix + sessionId;
//        return preKey.getBytes();
        return stringSerializer.serialize(preKey);
    }

    /**
     * 获得byte[]型的value
     * @param value
     * @return
     */
    private byte[] convertByteValue(String value){
        return stringSerializer.serialize(value);
    }


    /**
     * Returns the Redis session keys
     * prefix.
     * @return The prefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * Sets the Redis sessions key
     * prefix.
     * @param keyPrefix The prefix
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    private void setByteValue(int dbindex,byte[] key,byte[] value,int expire){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(dbindex);
            jedis.setex(key, expire, value);
        } catch (Exception e) {
            logger.error("",e);
        }finally {
            jedis.close();
        }
    }

    private void removeByteKey(int dbindex,byte[] key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(dbindex);
            jedis.del(key);
        } catch (Exception e) {
            logger.error("",e);
        }finally {
            jedis.close();
        }
    }

    private Set<byte[]> keys(int dbindex, byte[] key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(dbindex);
            Set<byte[]> set=jedis.keys(key);
            return set;
        } catch (Exception e) {
            logger.error("",e);
        }finally {
            jedis.close();
        }
        return null;
    }

    private String getValue(int dbindex,byte[] key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(dbindex);
            byte[] value=jedis.get(key);
            return stringSerializer.deserialize(value);
        } catch (SerializationException e) {
            logger.error("",e);
        }finally {
            jedis.close();
        }
        return null;
    }

}
