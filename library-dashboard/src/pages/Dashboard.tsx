import { useState, useEffect } from 'react';
import api from '../api/client';
import { useAuthStore } from '../store/authStore';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import toast, { Toaster } from 'react-hot-toast';  // ← ONLY THIS LINE for toast
interface Book {
  id: string;
  title: string;
  author: string;
  availableCopies: number;
  totalCopies: number;
  isbn?: string;
  genre?: string;
}

export default function Dashboard() {
  const { logout } = useAuthStore();
  const [search, setSearch] = useState('');
  const queryClient = useQueryClient();

  // Fetch books
  const { data: books = [], isLoading } = useQuery<Book[]>({
    queryKey: ['books'],
    queryFn: async () => {
      const res = await api.get('/books');
      return res.data;
    },
  });

  // Borrow mutation
  const borrowMutation = useMutation({
    mutationFn: async (bookId: string) => {
      await api.post(`/loans/${bookId}`, {});
    },
    onSuccess: () => {
      toast.success('Book borrowed successfully!');
      queryClient.invalidateQueries({ queryKey: ['books'] });  // Refresh list
    },
    onError: (err: any) => {
      toast.error(err.response?.data || 'Failed to borrow book');
    },
  });

  const filteredBooks = books.filter(book =>
    book.title.toLowerCase().includes(search.toLowerCase()) ||
    book.author.toLowerCase().includes(search.toLowerCase())
  );

return (
  <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100">
    {/* Header */}
    <header className="bg-gradient-to-r from-blue-700 to-purple-800 text-white shadow-xl">
      <div className="max-w-7xl mx-auto px-6 py-6 flex justify-between items-center">
        <div>
          <h1 className="text-4xl font-bold">Library Catalog</h1>
          <p className="text-blue-100 mt-1">Discover your next great read</p>
        </div>
        <button
          onClick={logout}
          className="bg-red-600 hover:bg-red-700 px-6 py-3 rounded-full font-medium transition transform hover:scale-105"
        >
          Logout
        </button>
      </div>
    </header>

    {/* Search */}
<div className="max-w-7xl mx-auto px-6 py-8">
  <div className="relative flex justify-center">
    <input
      type="text"
      placeholder="Search by title or author..."
      value={search}
      onChange={(e) => setSearch(e.target.value)}
      className="w-full max-w-2xl px-6 py-4 text-lg bg-white text-gray-900 placeholder-gray-500 border-0 rounded-full shadow-lg focus:outline-none focus:ring-4 focus:ring-blue-300 transition"
    />
    <svg className="absolute right-4 top-5 w-6 h-6 text-gray-500 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
    </svg>
  </div>
</div>

    {/* Books Grid */}
    <div className="max-w-7xl mx-auto px-6 pb-16">
      {isLoading ? (
        <div className="text-center py-20">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-4 border-blue-600"></div>
          <p className="mt-4 text-xl text-gray-600">Loading books...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
          {filteredBooks.map((book) => (
            <div key={book.id} className="group bg-white rounded-2xl shadow-lg overflow-hidden hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2">
              <div className="bg-gradient-to-br from-blue-400 to-purple-500 h-48 flex items-center justify-center">
                <div className="text-white text-6xl font-bold opacity-20">BOOK</div>
              </div>
              <div className="p-6">
                <h3 className="font-bold text-xl text-gray-800 mb-2 line-clamp-2">{book.title}</h3>
                <p className="text-gray-600 mb-3">by {book.author}</p>
                <div className="flex items-center justify-between mb-4">
                  <span className="text-sm text-gray-500">
                    Available: <span className="font-bold text-green-600">{book.availableCopies}</span> / {book.totalCopies}
                  </span>
                </div>
                <button
                  onClick={() => borrowMutation.mutate(book.id)}
                  disabled={borrowMutation.isPending || book.availableCopies === 0}
                  className={`w-full py-3 rounded-lg font-semibold transition-all ${
                    book.availableCopies > 0
                      ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white hover:from-blue-700 hover:to-purple-700 transform hover:scale-105'
                      : 'bg-gray-300 text-gray-600 cursor-not-allowed'
                  }`}
                >
                  {borrowMutation.isPending ? 'Borrowing...' : 
                   book.availableCopies > 0 ? 'Borrow Book' : 'Not Available'}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {filteredBooks.length === 0 && !isLoading && (
        <div className="text-center py-32">
          <div className="text-6xl mb-4">📚</div>
          <p className="text-2xl text-gray-600">No books found</p>
          <p className="text-gray-500 mt-2">Try adjusting your search</p>
        </div>
      )}
    </div>
    <Toaster position="bottom-right" toastOptions={{
      style: {
        background: '#363636',
        color: '#fff',
      },
    }} />
  </div>
);
}

