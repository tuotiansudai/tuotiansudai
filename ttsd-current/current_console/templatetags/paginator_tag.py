# coding=utf-8
from django.template.defaulttags import register


@register.inclusion_tag('console/tags/paginator.html')
def paginator_widget(request, data):
    params = generate_params(request)
    page = int(request.GET.get('page', 1))
    count = data.get('count', 0)
    total_pages = count / 1 if count % 1 == 0 else count / 1 + 1
    return {
        'params': params,
        'page': page,
        'previous_page': page - 1,
        'next_page': page + 1,
        'has_previous': data.get('previous', None) is not None,
        'has_next': data.get('next', None) is not None,
        'page_range': calculate_page_range(total_pages, page, 5),
        'total_pages': total_pages
    }


def generate_params(request):
    param_dict = request.GET.dict()
    param_dict.pop('page', None)
    params = ""
    for k, v in param_dict.items():
        params += "&" + k + "=" + v
    return params


def calculate_page_range(max_page, current=1, max_show_page=5):
    if max_page <= max_show_page:
        return list(range(1, max_page + 1))

    start_page = current - (max_show_page - 1) / 2
    page_range = range(start_page, start_page + max_show_page)

    step = 1 if page_range[0] < 1 else 0
    step = -1 if page_range[-1] > max_page else step

    while True:
        if len(filter(lambda page: page < 1 or page > max_page, page_range)) == 0:
            return page_range

        for index in range(0, len(page_range)):
            page_range[index] += step
