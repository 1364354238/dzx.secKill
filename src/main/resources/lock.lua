if redis.call("get",KEYS[1])==ARGV[1] then
    return redis.call("del",kEYS[1])
else
    return 0
end