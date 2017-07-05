from collections import OrderedDict

from rest_framework import pagination
from rest_framework.response import Response


class ExPageNumberPagination(pagination.PageNumberPagination):
    def get_paginated_response(self, data):
        return Response(OrderedDict([
            ('count', self.page.paginator.count),
            ('has_next', self.page.has_next()),
            ('has_previous', self.page.has_previous()),
            ('page', self.page.number),
            ('page_count', self.page.paginator.num_pages),
            ('results', data)
        ]))
