<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="contract-list.js" headLab="project-manage" sideLab="contract" title="CFCA创建合同">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">标的ID:</label>
                <input type="text" name="loanId" class="form-control jq-id">
            </div>

            <div class="form-group">
                <label for="control-label">标的类型:</label>
                <select class="selectpicker" name="anxinContractType">
                    <option value="LOAN_CONTRACT">普通标的</option>
                    <option value="TRANSFER_CONTRACT">债权转让</option>
                </select>
            </div>
            <div class="form-group">
                <button class="btn btn-default" type="button" id="create">创建合同</button>
            </div>

            <div class="form-group">
                <button class="btn btn-default" type="button" id="find">查询并更新合同</button>
            </div>
        </div>
    </form>
</div>

</@global.main>