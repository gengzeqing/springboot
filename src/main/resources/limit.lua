local key= KEYS[1] --����KEY
local limit = tonumber(ARGV[1]) -- ������С
    local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then -- �������������С
   return 0
else --������+1,������2�����
   redis.call("INCRBY",key,"1") --�����1
   redis.call("EXPIRE",key,"2") -- ���ù���ʱ��
   return 1
end