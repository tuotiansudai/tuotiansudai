
# 定义不同QA环境的独立配置：
individual_config = {
    "common.environment":{
        "QA1":"QA",
        "QA2":"QA",
        "QA3":"QA",
        "QA4":"QA",
        "QA5":"QA"
    },

}




def modifyip(tfile, sstr, rstr):
    try:
        lines = open(tfile, 'r').readlines()
        flen = len(lines) - 1
        for i in range(flen):
            if sstr in lines[i]:
                lines[i] = lines[i].replace(sstr, rstr)
        open(tfile, 'w').writelines(lines)

    except Exception, e:
        print e



modifyip('aa', 'aaa', 'ccc')